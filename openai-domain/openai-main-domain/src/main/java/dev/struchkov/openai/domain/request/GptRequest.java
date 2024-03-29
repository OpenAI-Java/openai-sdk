package dev.struchkov.openai.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.struchkov.openai.domain.common.GptMessage;
import dev.struchkov.openai.domain.model.gpt.GPTModel;
import dev.struchkov.openai.domain.request.format.ResponseFormat;
import dev.struchkov.openai.domain.request.picture.PictureSize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GptRequest {

    private String model;

    private String prompt;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    private Double temperature;

    private List<GptMessage> messages;

    @JsonProperty("top_p")
    private Double topP;

    private Integer n;

    private Boolean stream;

    private List<String> stop;

    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    @JsonProperty("logit_bias")
    private Map<Long, Long> logitBias;

    private String user;

    private PictureSize size;

    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

}
