package dev.struchkov.openai;

import dev.struchkov.openai.context.ChatGptService;
import dev.struchkov.openai.context.GPTClient;
import dev.struchkov.openai.context.data.ChatGptStorage;
import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;
import dev.struchkov.openai.domain.common.GptMessage;
import dev.struchkov.openai.domain.model.gpt.GPT3Model;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.Choice;
import dev.struchkov.openai.domain.response.GptResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static dev.struchkov.haiti.utils.Checker.checkNotNull;

@RequiredArgsConstructor
public class ChatGptServiceImpl implements ChatGptService {

    @Setter
    private Long contextLimit;
    private final GPTClient gptClient;
    private final ChatGptStorage chatStorage;

    @Override
    public ChatInfo createChat() {
        return chatStorage.save(ChatInfo.builder().build());
    }

    @Override
    public String sendNewMessage(@NonNull UUID chatId, @NonNull String message) {
        final List<GptMessage> gptMessages = generateGptMessages(chatId, message);

        final GptResponse gptResponse = gptClient.execute(
                GptRequest.builder()
                        .messages(gptMessages)
                        .model(GPT3Model.GPT_3_5_TURBO)
                        .build()
        );

        final List<Choice> choices = gptResponse.getChoices();
        final GptMessage answer = choices.get(choices.size() - 1).getMessage();
        final ChatMessage answerMessage = convert(chatId, answer);
        return chatStorage.save(answerMessage).getMessage();
    }

    @Override
    public CompletableFuture<String> sendNewMessageAsync(@NonNull UUID chatId, @NonNull String message) {
        final List<GptMessage> gptMessages = generateGptMessages(chatId, message);

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
                    return chatStorage.save(answerMessage).getMessage();
                }
        );
    }

    @NotNull
    private List<GptMessage> generateGptMessages(@NotNull UUID chatId, @NotNull String message) {
        final ChatMessage chatMessage = ChatMessage.builder()
                .chatId(chatId)
                .role("user")
                .message(message)
                .build();
        chatStorage.save(chatMessage);
        final List<ChatMessage> historyMessages = chatStorage.findAllMessage(chatId);

        if (checkNotNull(contextLimit) && (historyMessages.size() > contextLimit)) {
            final long delta = historyMessages.size() - contextLimit;
            for (int i = 0; i < delta; i++) {
                chatStorage.removeMessage(chatId, historyMessages.get(i).getMessageId());
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

    @Override
    public void closeChat(@NonNull UUID chatId) {
        chatStorage.remove(chatId);
    }

    @Override
    public long getCountMessages(@NonNull UUID chatId) {
        return chatStorage.countMessagesByChatId(chatId);
    }

}
