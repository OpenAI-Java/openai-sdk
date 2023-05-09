package dev.struchkov.openai.quarkus.context.service;

import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.CreateChat;
import dev.struchkov.openai.domain.message.AnswerMessage;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;

import java.util.UUID;

public interface ChatGptService {

    Uni<ChatInfo> createChat(CreateChat createChat);

    Uni<ChatInfo> updateChat(ChatInfo updateChat);

    Uni<ChatInfo> getChatById(@NonNull UUID chatId);

    Uni<AnswerMessage> sendNewMessage(@NonNull UUID chatId, @NonNull String message);

    Multi<String> sendNewMessageStream(@NonNull UUID chatId, @NonNull String message);

    Uni<AnswerMessage> regenerateMessage(@NonNull UUID chatId, @NonNull UUID messageId);

    Uni<AnswerMessage> continueThought(@NonNull UUID chatId);

    Uni<Void> closeChat(@NonNull UUID chatId);

    Uni<Long> getCountMessages(@NonNull UUID chatId);

    Uni<Void> clearContext(@NonNull UUID chatId);

    //TODO [01.05.2023|uPagge]: Возможно нужно куда-то перенести этот метод

    /**
     * Позволяет выполнить одиночный запрос без создания чата.
     */
    Uni<AnswerMessage> sendSingleMessage(String message);

}
