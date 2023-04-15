package dev.struchkov.quarkus.openai.impl;

import com.github.f4b6a3.uuid.UuidCreator;
import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;
import dev.struchkov.openai.domain.chat.CreateChat;
import dev.struchkov.openai.domain.common.GptMessage;
import dev.struchkov.openai.domain.message.AnswerChatMessage;
import dev.struchkov.openai.domain.model.gpt.GPT3Model;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.Choice;
import dev.struchkov.openai.quarkus.context.GPTClient;
import dev.struchkov.openai.quarkus.context.data.ChatGptStorage;
import dev.struchkov.openai.quarkus.context.service.ChatGptService;
import dev.struchkov.quarkus.openai.BaseGptService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static dev.struchkov.haiti.utils.Checker.checkNotBlank;
import static dev.struchkov.haiti.utils.Checker.checkNotNull;

public class ChatGptServiceImpl extends BaseGptService implements ChatGptService {

    private final ChatGptStorage chatStorage;

    public ChatGptServiceImpl(GPTClient client, ChatGptStorage chatStorage) {
        super(client);
        this.chatStorage = chatStorage;
    }

    @Override
    public Uni<ChatInfo> createChat(CreateChat createChat) {
        return chatStorage.save(
                ChatInfo.builder()
                        .chatId(checkNotNull(createChat.getChatId()) ? createChat.getChatId() : UuidCreator.getTimeOrderedEpochPlus1())
                        .contextConstraint(createChat.getContextConstraint())
                        .systemBehavior(createChat.getSystemBehavior())
                        .build()
        );
    }

    @Override
    public Uni<AnswerChatMessage> sendNewMessage(@NonNull UUID chatId, @NonNull String message) {
        return chatStorage.findChatInfoById(chatId)
                .onItem().ifNull().fail()
                .flatMap(chatInfo -> generateGptMessages(chatInfo, message)
                        .map(list -> {
                            final String systemBehavior = chatInfo.getSystemBehavior();
                            if (checkNotBlank(systemBehavior)) list.add(0, GptMessage.fromSystem(systemBehavior));
                            return GptRequest.builder()
                                    .messages(list)
                                    .model(GPT3Model.GPT_3_5_TURBO)
                                    .build();
                        }))
                .flatMap(client::executeChat)
                .flatMap(gptResponse -> {
                    final List<Choice> choices = gptResponse.getChoices();
                    return chatStorage.save(convert(chatId, choices.get(choices.size() - 1).getMessage()))
                            .map(chatMsg -> AnswerChatMessage.builder()
                                    .message(chatMsg.getMessage())
                                    .usage(gptResponse.getUsage())
                                    .build());
                });
    }

    @Override
    public Multi<String> sendNewMessageStream(@NonNull UUID chatId, @NonNull String message) {
        final StringBuilder sb = new StringBuilder();
        return chatStorage.findChatInfoById(chatId)
                .onItem().ifNull().fail()
                .flatMap(chatInfo -> generateGptMessages(chatInfo, message)
                        .map(list -> {
                            list.add(0, GptMessage.fromSystem(chatInfo.getSystemBehavior()));
                            return GptRequest.builder()
                                    .messages(list)
                                    .model(GPT3Model.GPT_3_5_TURBO)
                                    .stream(true)
                                    .build();
                        }))
                .onItem().transformToMulti(client::executeChatStream)
                .map(gptResponse -> {
                    final List<Choice> choices = gptResponse.getChoices();
                    final String msgPart = choices.get(choices.size() - 1).getDelta().getContent();
                    sb.append(msgPart);
                    return msgPart;
                })
                .onCompletion().call(() -> {
                    final ChatMessage msg = convert(chatId, GptMessage.builder().content(sb.toString()).build());
                    return chatStorage.save(msg);
                });
    }

    @Override
    public Uni<Void> closeChat(@NonNull UUID chatId) {
        return chatStorage.remove(chatId);
    }

    @Override
    public Uni<Long> getCountMessages(@NonNull UUID chatId) {
        return chatStorage.countMessagesByChatId(chatId);
    }

    @Override
    public Uni<Void> clearContext(@NonNull UUID chatId) {
        return chatStorage.removeAllMessages(chatId);
    }

    private Uni<List<GptMessage>> generateGptMessages(@NotNull ChatInfo chatInfo, @NotNull String message) {
        final ChatMessage chatMessage = ChatMessage.builder()
                .chatId(chatInfo.getChatId())
                .role("user")
                .message(message)
                .build();
        return chatStorage.save(chatMessage)
                .onItem().transformToMulti(ignore -> chatStorage.findAllMessage(chatInfo.getChatId()))
                .collect().asList()
                .flatMap(historyMessages -> {
                    final Long contextConstraint = chatInfo.getContextConstraint();
                    if (checkNotNull(contextConstraint) && (historyMessages.size() > contextConstraint)) {
                        final long delta = historyMessages.size() - contextConstraint;
                        for (int i = 0; i < delta; i++) {
                            chatStorage.removeMessage(chatInfo.getChatId(), historyMessages.get(i).getMessageId());
                        }
                    }
                    return chatStorage.findAllMessage(chatInfo.getChatId())
                            .map(ChatGptServiceImpl::convert)
                            .collect().asList();
                });
    }

    private ChatMessage convert(UUID chatId, GptMessage answer) {
        return ChatMessage.builder()
                .chatId(chatId)
                .role(answer.getRole())
                .message(answer.getContent())
                .build();
    }

    private static GptMessage convert(ChatMessage mes) {
        return GptMessage.builder()
                .role(mes.getRole())
                .content(mes.getMessage())
                .build();
    }

}
