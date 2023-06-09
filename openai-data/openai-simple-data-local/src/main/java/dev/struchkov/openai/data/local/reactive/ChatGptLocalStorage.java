package dev.struchkov.openai.data.local.reactive;

import com.github.f4b6a3.uuid.UuidCreator;
import dev.struchkov.openai.domain.chat.MainChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;
import dev.struchkov.openai.quarkus.context.data.MainChatGptStorage;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

//TODO [09.04.2023|uPagge]: Перенести в свой модуль
public class ChatGptLocalStorage implements MainChatGptStorage {

    private final Map<UUID, MainChatInfo> chatMap = new HashMap<>();
    private final Map<UUID, TreeMap<UUID, ChatMessage>> messageMap = new HashMap<>();

    @Override
    public Uni<MainChatInfo> save(MainChatInfo mainChatInfo) {
        return Uni.createFrom().item(() -> {
            final UUID uuid = UuidCreator.getTimeOrderedEpochPlus1();
            mainChatInfo.setChatId(uuid);
            messageMap.put(uuid, new TreeMap<>());
            chatMap.put(uuid, mainChatInfo);
            return mainChatInfo;
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
    public Uni<ChatMessage> findMessageById(UUID chatId, UUID messageId) {
        return Uni.createFrom().item(messageMap.get(chatId).get(messageId));
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
    public Uni<Void> removeMessage(UUID chatId, UUID messageId) {
        if (messageMap.containsKey(chatId)) {
            messageMap.get(chatId).remove(messageId);
        }
        return Uni.createFrom().voidItem();
    }

    @Override
    public Uni<MainChatInfo> findChatInfoById(UUID chatId) {
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

    @Override
    public Uni<Void> deleteAllByChatIdAndDateAdded(UUID chatId, LocalDateTime dateAdded) {
        return null;
    }

}
