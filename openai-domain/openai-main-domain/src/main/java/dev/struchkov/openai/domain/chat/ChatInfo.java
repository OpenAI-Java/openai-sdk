package dev.struchkov.openai.domain.chat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatInfo {

    private UUID chatId;

    /**
     * Ограничение контекста обсуждения. Задает сколько всего сообщений сохраняется в чате. Старые сообщения постепенно удаляются.
     */
    private Long contextConstraint;

    private Double temperature;

    private String userId;

    @ToString.Exclude
    private String systemBehavior;

}
