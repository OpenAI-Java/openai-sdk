package dev.struchkov.openai.domain.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ChatMessage {

    private UUID messageId;
    private UUID chatId;
    private String role;
    private String message;

}
