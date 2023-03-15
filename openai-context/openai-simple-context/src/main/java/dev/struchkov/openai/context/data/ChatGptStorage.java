package dev.struchkov.openai.context.data;

import dev.struchkov.openai.domain.chat.ChatInfo;
import dev.struchkov.openai.domain.chat.ChatMessage;

import java.util.List;
import java.util.UUID;

public interface ChatGptStorage {

    ChatInfo save(ChatInfo build);

    ChatMessage save(ChatMessage chatMessage);

    List<ChatMessage> findAllMessage(UUID chatId);

    void remove(UUID chatId);

    long countMessagesByChatId(UUID chatId);

}
