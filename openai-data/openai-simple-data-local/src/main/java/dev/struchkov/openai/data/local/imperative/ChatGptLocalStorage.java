package dev.struchkov.openai.data.local.imperative;

import com.github.f4b6a3.uuid.UuidCreator;
import dev.struchkov.openai.context.data.ChatGptStorage;
import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

public class ChatGptLocalStorage implements ChatGptStorage {

    private final Map<UUID, ChatInfo> chatMap = new HashMap<>();
    private final Map<UUID, TreeMap<UUID, ChatMessage>> messageMap = new HashMap<>();

    @Override
    public ChatInfo save(ChatInfo chatInfo) {
        final UUID uuid = UuidCreator.getTimeOrderedEpochPlus1();
        chatInfo.setChatId(uuid);
        messageMap.put(uuid, new TreeMap<>());
        chatMap.put(uuid, chatInfo);
        return chatInfo;
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        final UUID uuid = UuidCreator.getTimeOrderedEpochPlus1();
        chatMessage.setMessageId(uuid);
        messageMap.get(chatMessage.getChatId()).put(uuid, chatMessage);
        return chatMessage;
    }

    @Override
    public List<ChatMessage> findAllMessage(UUID chatId) {
        return messageMap.get(chatId).values().stream().toList();
    }

    @Override
    public void remove(UUID chatId) {
        chatMap.remove(chatId);
        messageMap.remove(chatId);
    }

    @Override
    public long countMessagesByChatId(UUID chatId) {
        if (messageMap.containsKey(chatId)) {
            return messageMap.get(chatId).values().size();
        }
        return 0;
    }

    @Override
    public void removeMessage(UUID chatId, UUID messageId) {
        if (messageMap.containsKey(chatId)) {
            messageMap.get(chatId).remove(messageId);
        }
    }

    @Override
    public Optional<ChatInfo> findChatInfoById(UUID chatId) {
        return Optional.ofNullable(chatMap.get(chatId));
    }

}
