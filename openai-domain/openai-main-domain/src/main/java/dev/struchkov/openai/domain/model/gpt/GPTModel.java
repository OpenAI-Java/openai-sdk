package dev.struchkov.openai.domain.model.gpt;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.struchkov.openai.domain.model.AIModel;
import dev.struchkov.openai.domain.model.ModelDeserializer;

@JsonDeserialize(using = ModelDeserializer.class)
public interface GPTModel extends AIModel {

}
