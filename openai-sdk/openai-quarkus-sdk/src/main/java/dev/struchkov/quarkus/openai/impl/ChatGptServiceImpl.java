package dev.struchkov.quarkus.openai.impl;

import com.github.f4b6a3.uuid.UuidCreator;
import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;
import dev.struchkov.openai.domain.chat.CreateChat;
import dev.struchkov.openai.domain.common.GptMessage;
import dev.struchkov.openai.domain.message.AnswerMessage;
import dev.struchkov.openai.domain.model.gpt.GPT3Model;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.Choice;
import dev.struchkov.openai.domain.response.GptResponse;
import dev.struchkov.openai.quarkus.context.GPTClient;
import dev.struchkov.openai.quarkus.context.data.ChatGptStorage;
import dev.struchkov.openai.quarkus.context.service.ChatGptService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dev.struchkov.haiti.utils.Checker.checkNotBlank;
import static dev.struchkov.haiti.utils.Checker.checkNotNull;

@Slf4j
@RequiredArgsConstructor
public class ChatGptServiceImpl implements ChatGptService {

    protected final GPTClient client;
    private final ChatGptStorage chatStorage;

    @Override
    public Uni<ChatInfo> createChat(CreateChat createChat) {
        return chatStorage.save(
                ChatInfo.builder()
                        .chatId(checkNotNull(createChat.getChatId()) ? createChat.getChatId() : UuidCreator.getTimeOrderedEpochPlus1())
                        .temperature(checkNotNull(createChat.getTemperature()) ? createChat.getTemperature() : 1.0)
                        .userId(createChat.getUserId())
                        .contextConstraint(createChat.getContextConstraint())
                        .systemBehavior(createChat.getSystemBehavior())
                        .build()
        ).invoke(chatInfo -> log.debug("Был создан новый чат: {}", chatInfo));
    }

    @Override
    public Uni<ChatInfo> updateChat(ChatInfo updateChat) {
        return chatStorage.findChatInfoById(updateChat.getChatId())
                .flatMap(existChatInfo -> {
                    existChatInfo.setSystemBehavior(updateChat.getSystemBehavior());
                    existChatInfo.setContextConstraint(updateChat.getContextConstraint());
                    existChatInfo.setTemperature(updateChat.getTemperature());
                    return chatStorage.save(existChatInfo);
                });
    }

    @Override
    public Uni<ChatInfo> getChatById(@NonNull UUID chatId) {
        return chatStorage.findChatInfoById(chatId)
                .invoke(() -> log.trace("Получение чата по идентификатору: {}", chatId));
    }

    @Override
    public Uni<AnswerMessage> sendNewMessage(@NonNull UUID chatId, @NonNull String message) {
        return chatStorage.findChatInfoById(chatId)
                .onItem().ifNull().fail()
                .call(chatInfo -> createNewUserChatMessage(message, chatInfo))
                .flatMap(chatInfo -> generateGptRequest(chatInfo, false))
                .flatMap(client::executeChat)
                .flatMap(gptResponse -> generateAnswerMessage(chatId, gptResponse));
    }

    @Override
    public Uni<AnswerMessage> regenerateMessage(@NonNull UUID chatId, @NonNull UUID messageId) {
        return chatStorage.findChatInfoById(chatId)
                .onItem().ifNull().fail()
                .call(
                        chatInfo -> chatStorage.findMessageById(chatId, messageId)
                                .call(message -> chatStorage.deleteAllByChatIdAndDateAdded(message.getChatId(), message.getDateAdded()))
                )
                .flatMap(chatInfo -> generateGptRequest(chatInfo, false))
                .flatMap(client::executeChat)
                .flatMap(gptResponse -> generateAnswerMessage(chatId, gptResponse));
    }

    @Override
    public Uni<AnswerMessage> continueThought(@NonNull UUID chatId) {
        return chatStorage.findChatInfoById(chatId)
                .onItem().ifNull().fail()
                .flatMap(chatInfo -> generateGptRequest(chatInfo, false))
                .flatMap(client::executeChat)
                .flatMap(gptResponse -> generateAnswerMessage(chatId, gptResponse));
    }

    @Override
    public Multi<String> sendNewMessageStream(@NonNull UUID chatId, @NonNull String message) {
        final StringBuilder sb = new StringBuilder();
        return chatStorage.findChatInfoById(chatId)
                .onItem().ifNull().fail()
                .call(chatInfo -> createNewUserChatMessage(message, chatInfo))
                .flatMap(chatInfo -> generateGptRequest(chatInfo, true))
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
        return chatStorage.remove(chatId)
                .invoke(() -> log.debug("Чат был закрыт: {}", chatId));
    }

    @Override
    public Uni<Long> getCountMessages(@NonNull UUID chatId) {
        return chatStorage.countMessagesByChatId(chatId);
    }

    @Override
    public Uni<Void> clearContext(@NonNull UUID chatId) {
        return chatStorage.removeAllMessages(chatId)
                .invoke(() -> log.debug("Контекст чата очищен: {}", chatId));
    }

    @Override
    public Uni<AnswerMessage> sendSingleMessage(String message) {
        return client.executeChat(
                GptRequest.builder()
                        .model(GPT3Model.GPT_3_5_TURBO)
                        .messages(List.of(
                                GptMessage.fromUser(message)
                        ))
                        .build()
        ).map(gptResponse -> {
            final List<Choice> choices = gptResponse.getChoices();
            final GptMessage gptAnswer = choices.get(choices.size() - 1).getMessage();
            return AnswerMessage.builder()
                    .message(gptAnswer.getContent())
                    .usage(gptResponse.getUsage())
                    .build();
        });
    }

    private Uni<ChatMessage> createNewUserChatMessage(@NotNull String message, ChatInfo chatInfo) {
        return chatStorage.save(
                ChatMessage.builder()
                        .dateAdded(LocalDateTime.now())
                        .chatId(chatInfo.getChatId())
                        .role("user")
                        .message(message)
                        .build()
        );
    }

    private Uni<GptRequest> generateGptRequest(ChatInfo chatInfo, boolean stream) {
        return generateGptMessages(chatInfo)
                .map(
                        gptMessages -> GptRequest.builder()
                                .messages(gptMessages)
                                .temperature(chatInfo.getTemperature())
                                .user(chatInfo.getUserId())
                                .model(GPT3Model.GPT_3_5_TURBO)
                                .stream(stream)
                                .build()
                );
    }

    private Uni<AnswerMessage> generateAnswerMessage(@NotNull UUID chatId, GptResponse gptResponse) {
        final List<Choice> choices = gptResponse.getChoices();
        return chatStorage.save(convert(chatId, choices.get(choices.size() - 1).getMessage()))
                .map(
                        chatMsg -> AnswerMessage.builder()
                                .chatId(chatMsg.getChatId())
                                .messageId(chatMsg.getMessageId())
                                .message(chatMsg.getMessage())
                                .usage(gptResponse.getUsage())
                                .build()
                );
    }

    private Uni<List<GptMessage>> generateGptMessages(@NotNull ChatInfo chatInfo) {
        return chatStorage.findAllMessage(chatInfo.getChatId()).collect().asList()
                .flatMap(historyMessages -> {
                    final Long contextConstraint = chatInfo.getContextConstraint();
                    if (checkNotNull(contextConstraint) && (historyMessages.size() > contextConstraint)) {
                        final long delta = historyMessages.size() - contextConstraint;
                        final List<Uni<Void>> removals = new ArrayList<>();
                        for (int i = 0; i < delta; i++) {
                            removals.add(chatStorage.removeMessage(chatInfo.getChatId(), historyMessages.get(i).getMessageId()));
                        }
                        return Uni.combine().all().unis(removals)
                                .discardItems()
                                .onItem().transformToUni(ignore -> {
                                    final List<ChatMessage> messagesAfterRemoval = historyMessages.subList((int) delta, historyMessages.size());
                                    return Multi.createFrom().iterable(messagesAfterRemoval)
                                            .map(ChatGptServiceImpl::convert)
                                            .collect().asList();
                                });
                    } else {
                        return Multi.createFrom().iterable(historyMessages)
                                .map(ChatGptServiceImpl::convert)
                                .collect().asList();
                    }
                }).invoke(gptMessages -> {
                    if (checkNotBlank(chatInfo.getSystemBehavior())) {
                        gptMessages.add(0, GptMessage.fromSystem(chatInfo.getSystemBehavior()));
                    }
                });
    }

    private ChatMessage convert(UUID chatId, GptMessage answer) {
        return ChatMessage.builder()
                .dateAdded(LocalDateTime.now())
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
