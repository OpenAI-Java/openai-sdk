package dev.struchkov.openai.util;

import dev.struchkov.openai.domain.model.gpt.GPT3Model;
import dev.struchkov.openai.domain.model.gpt.GPT4Model;
import dev.struchkov.openai.domain.model.gpt.GPTModel;
import dev.struchkov.openai.domain.model.gpt.InstructGPTModel;
import lombok.experimental.UtilityClass;

import java.util.stream.Stream;

@UtilityClass
public class Models {

    public GPTModel gptModel(String value) {
        return Stream.of(GPT3Model.values(), GPT4Model.values(), InstructGPTModel.values())
                .flatMap(Stream::of)
                .filter(model -> model.getValue().equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value for enum: %s".formatted(value)));
    }

}