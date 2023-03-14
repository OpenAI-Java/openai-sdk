package dev.struchkov.openai.domain.conf;

import dev.struchkov.openai.domain.model.gpt.GPTModel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GPTConfig extends OpenAIConfig {

    @Builder
    public GPTConfig(
            String url,
            String token,
            String organisation,
            GPTModel aiModel
    ) {
        super(url, token, organisation, aiModel);
    }

}
