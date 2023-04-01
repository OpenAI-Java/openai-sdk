package dev.struchkov.openai.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.struchkov.openai.domain.model.AIModel;
import dev.struchkov.openai.domain.model.gpt.GPT3Model;
import dev.struchkov.openai.domain.model.gpt.GPT4Model;

import java.io.IOException;
import java.util.Objects;

public class AIModelGsonDes extends StdDeserializer<AIModel> {


    public AIModelGsonDes() {
        this(null);
    }

    protected AIModelGsonDes(Class<AIModel> vc) {
        super(vc);
    }

    @Override
    public AIModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        try {
            final GPT3Model optGpt3 = GPT3Model.findByValue(jsonParser.getValueAsString());
            if (Objects.nonNull(optGpt3)) {
                return optGpt3;
            }

            final GPT4Model optGpt4 = GPT4Model.findByValue(jsonParser.getValueAsString());
            if (Objects.nonNull(optGpt4)) {
                return optGpt4;
            }
            throw new RuntimeException("Invalid value for enum MyEnum: " + jsonParser.getText());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid value for enum MyEnum: " + jsonParser.getText());
        }
    }

}
