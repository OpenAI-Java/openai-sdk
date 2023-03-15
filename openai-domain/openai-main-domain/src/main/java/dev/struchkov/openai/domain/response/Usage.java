package dev.struchkov.openai.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usage {

    private Long promptTokens;
    private Long completionTokens;
    private Long totalTokens;

}
