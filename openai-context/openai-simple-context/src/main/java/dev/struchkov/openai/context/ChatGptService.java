package dev.struchkov.openai.context;

import dev.struchkov.openai.domain.chat.ChatInfo;
import lombok.NonNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ChatGptService {

    ChatInfo createChat();

    String sendNewMessage(@NonNull UUID chatId, @NonNull String message);

    CompletableFuture<String> sendNewMessageAsync(@NonNull UUID chatId, @NonNull String message);

    void closeChat(@NonNull UUID chatId);

    long getCountMessages(@NonNull UUID chatId);

}
