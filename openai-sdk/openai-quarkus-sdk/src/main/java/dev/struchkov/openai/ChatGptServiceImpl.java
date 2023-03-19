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
import io.smallrye.mutiny.Uni;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ChatGptServiceImpl implements ChatGptService {

    private final GPTClient gptClient;
    private final ChatGptStorage chatStorage;

    @Override
    public Uni<ChatInfo> createChat() {
        return chatStorage.save(ChatInfo.builder().build());
    }

    @Override
    public Uni<String> sendNewMessage(@NonNull UUID chatId, @NonNull String message) {
        final ChatMessage chatMessage = ChatMessage.builder()
                .chatId(chatId)
                .role("user")
                .message(message)
                .build();

        return chatStorage.save(chatMessage)
                .onItem().transformToUni(
                        ignore -> chatStorage.findAllMessage(chatId)
                                .map(ChatGptServiceImpl::convert)
                                .collect().asList()
                                .map(gptMessages -> GptRequest.builder()
                                        .messages(gptMessages)
                                        .model(GPT3Model.GPT_3_5_TURBO)
                                        .build())
                                .flatMap(gptClient::execute)
                                .map(gptResponse -> {
                                    final List<Choice> choices = gptResponse.getChoices();
                                    return choices.get(choices.size() - 1).getMessage();
                                })
                                .map(gptMessage -> convert(chatId, gptMessage))
                                .flatMap(chatStorage::save)
                                .map(ChatMessage::getMessage)
                );
    }

    @Override
    public Uni<Void> closeChat(@NonNull UUID chatId) {
        return chatStorage.remove(chatId);
    }

    @Override
    public Uni<Long> getCountMessages(@NonNull UUID chatId) {
        return chatStorage.countMessagesByChatId(chatId);
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
