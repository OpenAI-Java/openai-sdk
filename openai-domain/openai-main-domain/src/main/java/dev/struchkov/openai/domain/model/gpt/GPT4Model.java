package dev.struchkov.openai.domain.model.gpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.struchkov.openai.domain.model.AIModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@RequiredArgsConstructor
public enum GPT4Model implements GPTModel {

    /**
     * More capable than any GPT-3.5 model, able to do more complex tasks, and optimized for chat. Will be updated with our latest model iteration..
     */
    GPT_4("gpt-4"),

    /**
     * Snapshot of gpt-4 from March 14th 2023. Unlike gpt-4, this model will not receive updates, and will only be supported for a three month period ending on June 14th 2023.
     */
    GPT_4_0314("gpt-4-0314"),

    /**
     * Same capabilities as the base gpt-4 mode but with 4x the context length. Will be updated with our latest model iteration.
     */
    GPT_4_32_K("gpt-4-32k"),

    /**
     * Snapshot of gpt-4-32 from March 14th 2023. Unlike gpt-4-32k, this model will not receive updates, and will only be supported for a three month period ending on June 14th 2023.
     */
    GPT_4_32_K_0314("gpt-4-32k-0314"),

    UNKNOWN("UNKNOWN");

    @Getter
    private final String value;

    private static final List<AIModel> ENUM_LIST = new ArrayList<>();

    static {
        ENUM_LIST.addAll(EnumSet.allOf(GPT4Model.class));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static GPT4Model findByValue(String value) {
        return (GPT4Model) AIModel.fromValue(value, ENUM_LIST, UNKNOWN);
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @Override
    public double getPrice() {
        return 0;
    }
}
