package dev.struchkov.openai.data.local.imperative;

import com.github.f4b6a3.uuid.UuidCreator;
import dev.struchkov.openai.context.data.ChatGptStorage;
import dev.struchkov.openai.domain.chat.MainChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

public class ChatGptLocalStorage implements ChatGptStorage {

    private final Map<UUID, MainChatInfo> chatMap = new HashMap<>();
    private final Map<UUID, TreeMap<UUID, ChatMessage>> messageMap = new HashMap<>();

    @Override
    public MainChatInfo save(MainChatInfo mainChatInfo) {
        final UUID uuid;
        if (mainChatInfo.getChatId() == null) {
            uuid = UuidCreator.getTimeOrderedEpochPlus1();
            mainChatInfo.setChatId(uuid);
            messageMap.put(uuid, new TreeMap<>());
            chatMap.put(uuid, mainChatInfo);
        } else {
            uuid = mainChatInfo.getChatId();
            chatMap.put(uuid, mainChatInfo);
        }
        return mainChatInfo;
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
    public Optional<MainChatInfo> findChatInfoById(UUID chatId) {
        return Optional.ofNullable(chatMap.get(chatId));
    }

    @Override
    public void removeAllMessages(UUID chatId) {
        if (messageMap.containsKey(chatId)) {
            messageMap.get(chatId).clear();
        }
    }

}
