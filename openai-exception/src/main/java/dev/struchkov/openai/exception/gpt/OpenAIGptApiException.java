package dev.struchkov.openai.exception.gpt;

import dev.struchkov.openai.exception.OpenAIApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OpenAIGptApiException extends OpenAIApiException {

    private String message;
    private String type;
    private String param;
    private String code;

}
