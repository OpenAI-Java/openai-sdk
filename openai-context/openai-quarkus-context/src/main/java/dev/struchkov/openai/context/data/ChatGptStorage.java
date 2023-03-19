package dev.struchkov.openai.context.data;

import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.util.UUID;

public interface ChatGptStorage {

    Uni<ChatInfo> save(ChatInfo build);

    Uni<ChatMessage> save(ChatMessage chatMessage);

    Multi<ChatMessage> findAllMessage(UUID chatId);

    Uni<Void> remove(UUID chatId);

    Uni<Long> countMessagesByChatId(UUID chatId);

}
