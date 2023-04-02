package dev.struchkov.openai.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.struchkov.openai.domain.model.gpt.GPTModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GptResponse {

    private String id;
//    private String text;
    private String object;
    private Long created;
    private GPTModel model;
    private Usage usage;
    private List<PictureData> data;
    private List<Choice> choices;

}
