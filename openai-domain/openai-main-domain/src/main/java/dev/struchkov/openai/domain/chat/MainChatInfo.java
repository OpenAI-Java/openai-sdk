package dev.struchkov.openai.domain.chat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MainChatInfo {

    protected UUID chatId;

    /**
     * Ограничение контекста обсуждения. Задает сколько всего сообщений сохраняется в чате. Старые сообщения постепенно удаляются.
     */
    protected Long contextConstraint;

    protected Double temperature;

    protected String userId;

    @ToString.Exclude
    protected String systemBehavior;

}
