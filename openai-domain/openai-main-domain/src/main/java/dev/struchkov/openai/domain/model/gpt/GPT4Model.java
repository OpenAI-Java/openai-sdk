package dev.struchkov.openai.domain.model.gpt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author upagge 14.03.2023
 */
@Getter
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
    GPT_4_32_K_0314("gpt-4-32k-0314");

    private final String value;

    public static Optional<GPT4Model> findByValue(String value) {
        return Arrays.stream(values())
                .filter(e -> e.getValue().equals(value))
                .findFirst();
    }

}
