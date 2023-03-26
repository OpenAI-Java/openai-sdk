package dev.struchkov.openai.quarkus.context;

import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.CreateChat;
import dev.struchkov.openai.domain.request.GptRequest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public interface ChatGptService {

    Uni<ChatInfo> createChat(CreateChat createChat);

    Uni<String> sendNewMessage(@NonNull UUID chatId, @NonNull String message);

    Uni<Void> closeChat(@NonNull UUID chatId);

    Uni<Long> getCountMessages(@NonNull UUID chatId);

    Multi<String> sendNewMessageStream(@NonNull UUID chatId, @NonNull String message);

    Uni<List<String>> generateImage(GptRequest request);

}
