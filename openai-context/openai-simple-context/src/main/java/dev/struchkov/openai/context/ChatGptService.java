package dev.struchkov.openai.context;

import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.CreateChat;
import dev.struchkov.openai.domain.message.AnswerMessage;
import lombok.NonNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ChatGptService {

    ChatInfo createChat(CreateChat createChat);

    ChatInfo updateChat(ChatInfo updateChat);

    AnswerMessage sendNewMessage(@NonNull UUID chatId, @NonNull String message);

    CompletableFuture<AnswerMessage> sendNewMessageAsync(@NonNull UUID chatId, @NonNull String message);

    void clearContext(@NonNull UUID chatId);

    void closeChat(@NonNull UUID chatId);

    long getCountMessages(@NonNull UUID chatId);

}
