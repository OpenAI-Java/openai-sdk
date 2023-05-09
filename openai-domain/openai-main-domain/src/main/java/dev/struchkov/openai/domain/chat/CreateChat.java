package dev.struchkov.openai.domain.chat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateChat {

    /**
     * Если не задать, то будет задан uuid v7.
     */
    private UUID chatId;

    /**
     * Ограничение контекста обсуждения. Задает сколько всего сообщений сохраняется в чате. Старые сообщения постепенно удаляются.
     */
    private Long contextConstraint;

    private Double temperature;

    private String userId;

    private String systemBehavior;

}
