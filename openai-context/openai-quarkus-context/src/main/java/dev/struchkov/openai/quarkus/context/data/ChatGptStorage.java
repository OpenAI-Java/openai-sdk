package dev.struchkov.openai.quarkus.context.data;

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

    Uni<Void> removeMessage(UUID chatId, UUID messageId);

    Uni<ChatInfo> findChatInfoById(UUID chatId);

    Uni<Void> removeAllMessages(UUID chatId);

}
