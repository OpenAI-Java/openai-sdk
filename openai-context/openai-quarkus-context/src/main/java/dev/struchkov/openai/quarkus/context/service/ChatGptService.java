package dev.struchkov.openai.quarkus.context.service;

import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.CreateChat;
import dev.struchkov.openai.domain.message.AnswerChatMessage;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;

import java.util.UUID;

public interface ChatGptService {

    Uni<ChatInfo> createChat(CreateChat createChat);

    Uni<ChatInfo> getChatById(@NonNull UUID chatId);

    Uni<AnswerChatMessage> sendNewMessage(@NonNull UUID chatId, @NonNull String message);

    Multi<String> sendNewMessageStream(@NonNull UUID chatId, @NonNull String message);

    Uni<Void> closeChat(@NonNull UUID chatId);

    Uni<Long> getCountMessages(@NonNull UUID chatId);

    Uni<Void> clearContext(@NonNull UUID chatId);

}
