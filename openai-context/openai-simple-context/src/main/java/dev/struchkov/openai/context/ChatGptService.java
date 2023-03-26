package dev.struchkov.openai.context;

import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.CreateChat;
import dev.struchkov.openai.domain.chat.UpdateChat;
import dev.struchkov.openai.domain.message.AnswerChatMessage;
import lombok.NonNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ChatGptService {

    ChatInfo createChat(CreateChat createChat);

    ChatInfo updateChat(UpdateChat updateChat);

    AnswerChatMessage sendNewMessage(@NonNull UUID chatId, @NonNull String message);

    CompletableFuture<AnswerChatMessage> sendNewMessageAsync(@NonNull UUID chatId, @NonNull String message);

    void closeChat(@NonNull UUID chatId);

    long getCountMessages(@NonNull UUID chatId);

}
