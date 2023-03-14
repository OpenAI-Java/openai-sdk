package dev.struchkov.openai.domain.response.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GptErrorResponse {

    private GptErrorDetail error;

}
