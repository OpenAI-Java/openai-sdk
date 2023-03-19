package dev.struchkov.openai.context;

import dev.struchkov.openai.domain.chat.ChatInfo;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;

import java.util.UUID;

public interface ChatGptService {

    Uni<ChatInfo> createChat();

    Uni<String> sendNewMessage(@NonNull UUID chatId, @NonNull String message);

    Uni<Void> closeChat(@NonNull UUID chatId);

    Uni<Long> getCountMessages(@NonNull UUID chatId);

}
