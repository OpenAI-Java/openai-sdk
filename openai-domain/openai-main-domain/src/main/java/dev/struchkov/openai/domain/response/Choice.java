package dev.struchkov.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.struchkov.openai.domain.common.GptMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Choice {

    private GptMessage message;
    private String finishReason;
    private Long index;

}
