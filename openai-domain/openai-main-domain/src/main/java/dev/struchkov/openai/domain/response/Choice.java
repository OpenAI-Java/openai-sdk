package dev.struchkov.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.struchkov.openai.domain.common.GptMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Choice {

    private GptMessage message;
    private String text;
    private Delta delta;
    @JsonProperty("finish_reason")
    private String finishReason;
    private Long index;

}
