package dev.struchkov.openai.quarkus.context.service;

import dev.struchkov.openai.domain.chat.CreateMainChat;
import dev.struchkov.openai.domain.chat.MainChatInfo;
import dev.struchkov.openai.domain.message.AnswerMessage;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;

import java.util.UUID;

public interface MainChatGptService<T extends MainChatInfo, D extends CreateMainChat> {

    Uni<T> createChat(D createChat);

    Uni<T> updateChat(T updateChat);

    Uni<T> getChatById(@NonNull UUID chatId);

    Uni<AnswerMessage> sendNewMessage(@NonNull UUID chatId, @NonNull String message);

    Multi<String> sendNewMessageStream(@NonNull UUID chatId, @NonNull String message);

    Uni<AnswerMessage> regenerateMessage(@NonNull UUID chatId, @NonNull UUID messageId);

    Uni<AnswerMessage> continueThought(@NonNull UUID chatId);

    Uni<Void> closeChat(@NonNull UUID chatId);

    Uni<Long> getCountMessages(@NonNull UUID chatId);

    Uni<Void> clearContext(@NonNull UUID chatId);

}
