package dev.struchkov.openai.domain.conf;

import dev.struchkov.openai.domain.model.AIModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIConfig {

    private String url = "https://api.openai.com/v1/chat/completions";
    private String token;
    private String organisation;
    private AIModel aiModel;

}
