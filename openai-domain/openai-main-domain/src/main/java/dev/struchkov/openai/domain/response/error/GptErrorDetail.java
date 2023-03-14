package dev.struchkov.openai.domain.response.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GptErrorDetail {

    private String message;
    private String type;
    private String param;
    private String code;

}
