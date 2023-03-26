package dev.struchkov.quarkus.openai;

import dev.struchkov.openai.domain.chat.CreateChat;
import dev.struchkov.openai.domain.response.PictureData;
import dev.struchkov.quarkus.openai.adapter.ChatGptClientAdapter;
import dev.struchkov.openai.quarkus.context.ChatGptService;
import dev.struchkov.openai.quarkus.context.data.ChatGptStorage;
import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;
import dev.struchkov.openai.domain.common.GptMessage;
import dev.struchkov.openai.domain.model.gpt.GPT3Model;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.Choice;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class ChatGptServiceImpl implements ChatGptService {

    private final ChatGptClientAdapter adapter;
    private final ChatGptStorage chatStorage;

    @Override
    public Uni<ChatInfo> createChat(CreateChat createChat) {
        return chatStorage.save(ChatInfo.builder()
                .contextConstraint(createChat.getContextConstraint())
                .systemBehavior(createChat.getSystemBehavior())
                .build());
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
                                .flatMap(adapter::executeChat)
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

    @Override
    public Multi<String> sendNewMessageStream(@NonNull UUID chatId, @NonNull String message) {
        final ChatMessage chatMessage = ChatMessage.builder()
                .chatId(chatId)
                .role("user")
                .message(message)
                .build();
        final StringBuilder sb = new StringBuilder();
        return chatStorage.save(chatMessage)
                .flatMap(ignore -> chatStorage.findAllMessage(chatId)
                        .map(ChatGptServiceImpl::convert)
                        .collect().asList()
                        .map(gptMessages -> GptRequest.builder()
                                .messages(gptMessages)
                                .model(GPT3Model.GPT_3_5_TURBO)
                                .stream(true)
                                .build()))
                .onItem().transformToMulti(adapter::executeChatStream)
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
    public Uni<List<String>> generateImage(GptRequest request) {
        return adapter.executePicture(request)
                .map(resp -> Objects.isNull(request.getResponseFormat()) || "url".equals(request.getResponseFormat()) ?
                        resp.getData().stream().map(PictureData::getUrl).toList() :
                        resp.getData().stream().map(PictureData::getB64_json).toList()
                );
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
