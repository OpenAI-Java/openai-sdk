package dev.struchkov.openai.data.local.reactive;

import com.github.f4b6a3.uuid.UuidCreator;
import dev.struchkov.openai.quarkus.context.data.ChatGptStorage;
import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Builder
public class ChatGptLocalStorage implements ChatGptStorage {

    private final Map<UUID, ChatInfo> chatMap = new HashMap<>();
    private final Map<UUID, TreeMap<UUID, ChatMessage>> messageMap = new HashMap<>();

    @Override
    public Uni<ChatInfo> save(ChatInfo chatInfo) {
        return Uni.createFrom().item(() -> {
            final UUID uuid = UuidCreator.getTimeOrderedEpochPlus1();
            chatInfo.setChatId(uuid);
            messageMap.put(uuid, new TreeMap<>());
            chatMap.put(uuid, chatInfo);
            return chatInfo;
        });
    }

    @Override
    public Uni<ChatMessage> save(ChatMessage chatMessage) {
        return Uni.createFrom().item(() -> {
            final UUID uuid = UuidCreator.getTimeOrderedEpochPlus1();
            chatMessage.setMessageId(uuid);
            messageMap.get(chatMessage.getChatId()).put(uuid, chatMessage);
            return chatMessage;
        });
    }

    @Override
    public Multi<ChatMessage> findAllMessage(UUID chatId) {
        return Multi.createFrom().iterable(messageMap.get(chatId).values().stream().toList());
    }

    @Override
    public Uni<Void> remove(UUID chatId) {
        return Uni.createFrom().item(() -> {
            chatMap.remove(chatId);
            messageMap.remove(chatId);
            return null;
        });
    }

    @Override
    public Uni<Long> countMessagesByChatId(UUID chatId) {
        if (messageMap.containsKey(chatId)) {
            return Uni.createFrom().item(() -> (long) messageMap.get(chatId).values().size());
        }
        return Uni.createFrom().item(0L);
    }

    @Override
    public void removeMessage(UUID chatId, UUID messageId) {
            if (messageMap.containsKey(chatId)) {
                messageMap.get(chatId).remove(messageId);
            }
    }

    @Override
    public Uni<ChatInfo> findChatInfoById(UUID chatId) {
        return Uni.createFrom().item(() -> chatMap.get(chatId));
    }

    @Override
    public Uni<Void> removeAllMessages(UUID chatId) {
        return Uni.createFrom().item(() -> {
            if (messageMap.containsKey(chatId)) {
                messageMap.get(chatId).clear();
            }
            return null;
        });
    }

}
