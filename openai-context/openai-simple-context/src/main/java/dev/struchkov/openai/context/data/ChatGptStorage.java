package dev.struchkov.openai.context.data;

import dev.struchkov.openai.domain.chat.MainChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatGptStorage {

    MainChatInfo save(MainChatInfo build);

    ChatMessage save(ChatMessage chatMessage);

    List<ChatMessage> findAllMessage(UUID chatId);

    void remove(UUID chatId);

    long countMessagesByChatId(UUID chatId);

    void removeMessage(UUID chatId, UUID messageId);

    Optional<MainChatInfo> findChatInfoById(UUID chatId);

    void removeAllMessages(UUID chatId);

}
