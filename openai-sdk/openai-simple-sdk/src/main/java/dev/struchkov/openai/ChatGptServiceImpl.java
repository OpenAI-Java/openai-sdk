package dev.struchkov.openai;

import dev.struchkov.openai.context.ChatGptService;
import dev.struchkov.openai.context.GPTClient;
import dev.struchkov.openai.context.data.ChatGptStorage;
import dev.struchkov.openai.domain.chat.ChatMessage;
import dev.struchkov.openai.domain.chat.CreateMainChat;
import dev.struchkov.openai.domain.chat.MainChatInfo;
import dev.struchkov.openai.domain.common.GptMessage;
import dev.struchkov.openai.domain.message.AnswerMessage;
import dev.struchkov.openai.domain.model.gpt.GPT3Model;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.Choice;
import dev.struchkov.openai.domain.response.GptResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static dev.struchkov.haiti.utils.Checker.checkNotBlank;
import static dev.struchkov.haiti.utils.Checker.checkNotNull;

@RequiredArgsConstructor
public class ChatGptServiceImpl implements ChatGptService {

    private final GPTClient gptClient;
    private final ChatGptStorage chatStorage;

    @Override
    public MainChatInfo createChat(CreateMainChat createChat) {
        final MainChatInfo mainChatInfo = new MainChatInfo();
        mainChatInfo.setContextConstraint(createChat.getContextConstraint());
        mainChatInfo.setSystemBehavior(createChat.getSystemBehavior());
        mainChatInfo.setChatId(createChat.getChatId());
        mainChatInfo.setTemperature(createChat.getTemperature());
        return chatStorage.save(mainChatInfo);
    }

    @Override
    public MainChatInfo updateChat(MainChatInfo updateChat) {
        final MainChatInfo oldMainChatInfo = chatStorage.findChatInfoById(updateChat.getChatId()).orElseThrow();
        oldMainChatInfo.setSystemBehavior(updateChat.getSystemBehavior());
        oldMainChatInfo.setContextConstraint(updateChat.getContextConstraint());
        oldMainChatInfo.setTemperature(updateChat.getTemperature());
        return chatStorage.save(oldMainChatInfo);
    }

    @Override
    public AnswerMessage sendNewMessage(@NonNull UUID chatId, @NonNull String message) {
        final MainChatInfo mainChatInfo = chatStorage.findChatInfoById(chatId).orElseThrow();
        final List<GptMessage> gptMessageHistory = generateGptMessages(mainChatInfo, message);

        final List<GptMessage> gptMessages = new ArrayList<>(gptMessageHistory.size() + 1);
        if (checkNotBlank(mainChatInfo.getSystemBehavior())) {
            gptMessages.add(GptMessage.fromSystem(mainChatInfo.getSystemBehavior()));
        }
        gptMessages.addAll(gptMessageHistory);

        final GptResponse gptResponse = gptClient.execute(
                GptRequest.builder()
                        .messages(gptMessages)
                        .model(GPT3Model.GPT_3_5_TURBO)
                        .build()
        );

        final List<Choice> choices = gptResponse.getChoices();
        final GptMessage answer = choices.get(choices.size() - 1).getMessage();
        final ChatMessage answerMessage = convert(chatId, answer);
        final ChatMessage savedAnswer = chatStorage.save(answerMessage);
        return AnswerMessage.builder()
                .chatId(savedAnswer.getChatId())
                .messageId(savedAnswer.getMessageId())
                .message(savedAnswer.getMessage())
                .usage(gptResponse.getUsage())
                .build();
    }

    @Override
    public CompletableFuture<AnswerMessage> sendNewMessageAsync(@NonNull UUID chatId, @NonNull String message) {
        final MainChatInfo mainChatInfo = chatStorage.findChatInfoById(chatId).orElseThrow();
        final List<GptMessage> gptMessageHistory = generateGptMessages(mainChatInfo, message);

        final List<GptMessage> gptMessages = new ArrayList<>(gptMessageHistory.size() + 1);
        if (checkNotBlank(mainChatInfo.getSystemBehavior())) {
            gptMessages.add(GptMessage.fromSystem(mainChatInfo.getSystemBehavior()));
        }
        gptMessages.addAll(gptMessageHistory);

        return gptClient.executeAsync(
                GptRequest.builder()
                        .messages(gptMessages)
                        .model(GPT3Model.GPT_3_5_TURBO)
                        .build()
        ).thenApply(
                gptResponse -> {
                    final List<Choice> choices = gptResponse.getChoices();
                    final GptMessage answer = choices.get(choices.size() - 1).getMessage();
                    final ChatMessage answerMessage = convert(chatId, answer);
                    final ChatMessage savedAnswer = chatStorage.save(answerMessage);
                    return AnswerMessage.builder()
                            .chatId(savedAnswer.getChatId())
                            .messageId(savedAnswer.getMessageId())
                            .message(savedAnswer.getMessage())
                            .usage(gptResponse.getUsage())
                            .build();
                }
        );
    }

    @Override
    public void clearContext(@NonNull UUID chatId) {
        chatStorage.removeAllMessages(chatId);
    }

    @Override
    public void closeChat(@NonNull UUID chatId) {
        chatStorage.remove(chatId);
    }

    @Override
    public long getCountMessages(@NonNull UUID chatId) {
        return chatStorage.countMessagesByChatId(chatId);
    }

    private List<GptMessage> generateGptMessages(@NotNull MainChatInfo mainChatInfo, @NotNull String message) {
        final ChatMessage chatMessage = ChatMessage.builder()
                .chatId(mainChatInfo.getChatId())
                .role("user")
                .message(message)
                .build();
        chatStorage.save(chatMessage);
        final List<ChatMessage> historyMessages = chatStorage.findAllMessage(mainChatInfo.getChatId());

        final Long contextConstraint = mainChatInfo.getContextConstraint();
        if (checkNotNull(contextConstraint) && (historyMessages.size() > contextConstraint)) {
            final long delta = historyMessages.size() - contextConstraint;
            for (int i = 0; i < delta; i++) {
                chatStorage.removeMessage(mainChatInfo.getChatId(), historyMessages.get(i).getMessageId());
            }
        }

        return historyMessages.stream()
                .map(ChatGptServiceImpl::convert)
                .toList();
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
