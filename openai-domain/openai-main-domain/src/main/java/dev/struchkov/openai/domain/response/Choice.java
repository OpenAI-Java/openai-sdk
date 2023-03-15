package dev.struchkov.openai.domain.response;

import dev.struchkov.openai.domain.common.GptMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Choice {

    private GptMessage message;
    private String finishReason;
    private Long index;

}
