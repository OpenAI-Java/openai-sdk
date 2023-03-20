package dev.struchkov.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usage {

    private Long promptTokens;
    private Long completionTokens;
    private Long totalTokens;

}
