package dev.struchkov.openai.context;

import dev.struchkov.openai.domain.chat.MainChatInfo;
import dev.struchkov.openai.domain.chat.CreateMainChat;
import dev.struchkov.openai.domain.message.AnswerMessage;
import lombok.NonNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ChatGptService {

    MainChatInfo createChat(CreateMainChat createChat);

    MainChatInfo updateChat(MainChatInfo updateChat);

    AnswerMessage sendNewMessage(@NonNull UUID chatId, @NonNull String message);

    CompletableFuture<AnswerMessage> sendNewMessageAsync(@NonNull UUID chatId, @NonNull String message);

    void clearContext(@NonNull UUID chatId);

    void closeChat(@NonNull UUID chatId);

    long getCountMessages(@NonNull UUID chatId);

}
