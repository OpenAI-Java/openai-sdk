package dev.struchkov.openai.domain.request;

import dev.struchkov.openai.domain.common.GptMessage;
import dev.struchkov.openai.domain.model.gpt.GPTModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GptRequest {

    private GPTModel model;

    @Builder.Default
    private Double temperature = 0.7;

    @Singular
    private List<GptMessage> messages;

}
