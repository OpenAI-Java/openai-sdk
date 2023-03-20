package dev.struchkov.openai.data.local;

import com.github.f4b6a3.uuid.UuidCreator;
import dev.struchkov.openai.context.data.ChatGptStorageReactive;
import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@ApplicationScoped
public class ChatGptLocalStorageReactive implements ChatGptStorageReactive {

    private final Map<UUID, ChatInfo> chatMap = new HashMap<>();
    private final Map<UUID, TreeMap<UUID, ChatMessage>> messageMap = new HashMap<>();

    @Override
    public Uni<ChatInfo> save(ChatInfo chatInfo) {
        final UUID uuid = UuidCreator.getTimeOrderedEpochPlus1();
        chatInfo.setChatId(uuid);
        messageMap.put(uuid, new TreeMap<>());
        chatMap.put(uuid, chatInfo);
        return Uni.createFrom().item(chatInfo);
    }

    @Override
    public Uni<ChatMessage> save(ChatMessage chatMessage) {
        final UUID uuid = UuidCreator.getTimeOrderedEpochPlus1();
        chatMessage.setMessageId(uuid);
        messageMap.get(chatMessage.getChatId()).put(uuid, chatMessage);
        return Uni.createFrom().item(chatMessage);
    }

    @Override
    public Multi<ChatMessage> findAllMessage(UUID chatId) {
        return Multi.createFrom().iterable(messageMap.get(chatId).values().stream().toList());
    }

    @Override
    public Uni<Void> remove(UUID chatId) {
        chatMap.remove(chatId);
        messageMap.remove(chatId);
        return Uni.createFrom().voidItem();
    }

    @Override
    public Uni<Long> countMessagesByChatId(UUID chatId) {
        if (messageMap.containsKey(chatId)) {
            return Uni.createFrom().item((long) messageMap.get(chatId).values().size());
        }
        return Uni.createFrom().item(0L);
    }

}
