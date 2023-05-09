package dev.struchkov.openai.domain.chat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateMainChat {

    /**
     * Если не задать, то будет задан uuid v7.
     */
    protected UUID chatId;

    /**
     * Ограничение контекста обсуждения. Задает сколько всего сообщений сохраняется в чате. Старые сообщения постепенно удаляются.
     */
    protected Long contextConstraint;

    protected Double temperature;

    protected String userId;

    protected String systemBehavior;

}
