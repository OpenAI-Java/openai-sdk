package dev.struchkov.openai.domain.model.gpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum InstructGPTModel implements GPTModel {

    ADA("text-ada-001"),
    BABBAGE("text-babbage-001"),
    CURIE("text-curie-001");

    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static InstructGPTModel findByValue(String value) {
        return Arrays.stream(values())
                .filter(model -> model.getValue().equalsIgnoreCase(value))
                .findFirst().orElse(null);
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
