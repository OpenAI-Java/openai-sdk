package dev.struchkov.openai.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GptResponse {

    private List<Choice> choices;

}
