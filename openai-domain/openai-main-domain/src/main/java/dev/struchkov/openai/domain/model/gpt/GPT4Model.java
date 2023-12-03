package dev.struchkov.openai.domain.model.gpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum GPT4Model implements GPTModel {

    GPT_4_1106_PREVIEW("gpt-4-1106-preview"),
    GPT_4_VISION_PREVIEW("gpt-4-vision-preview"),
    GPT_4("gpt-4"),
    GPT_4_32_K("gpt-4-32k"),
    GPT_4_0613("gpt-4-0613"),
    GPT_4_32_K_0613("gpt-4-32k-0613");

    @Getter
    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static GPT4Model findByValue(String value) {
        return Arrays.stream(values())
                .filter(model -> model.getValue().equalsIgnoreCase(value))
                .findFirst().orElse(null);
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

}
