package dev.struchkov.openai.domain.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.struchkov.openai.domain.model.gpt.GPT3Model;
import dev.struchkov.openai.domain.model.gpt.GPT4Model;
import dev.struchkov.openai.domain.model.gpt.GPTModel;

import java.io.IOException;
import java.util.stream.Stream;

public class ModelDeserializer extends StdDeserializer<GPTModel> {

    public ModelDeserializer() {
        super(GPTModel.class);
    }

    @Override
    public GPTModel deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        final String value = ctx.readValue(p, String.class);
        return Stream.of(GPT3Model.values(), GPT4Model.values())
                .flatMap(Stream::of)
                .filter(model -> model.getValue().equalsIgnoreCase(value))
                .findAny()
                .orElseThrow();
    }

}
