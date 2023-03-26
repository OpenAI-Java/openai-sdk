package dev.struchkov.openai.domain.model.gpt;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.struchkov.openai.domain.deser.ModelDeserializer;
import dev.struchkov.openai.domain.model.AIModel;

@JsonDeserialize(using = ModelDeserializer.class)
public interface GPTModel extends AIModel {

    /**
     * Возвращает цену за 1 токен.
     */
    double getPrice();

}
