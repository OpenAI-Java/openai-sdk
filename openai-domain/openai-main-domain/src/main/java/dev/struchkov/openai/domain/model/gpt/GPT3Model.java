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
public enum GPT3Model implements GPTModel {

    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_3_5_TURBO_0301("gpt-3.5-turbo-0301"),
    TEXT_DAVINCI_003("text-davinci-003"),
    TEXT_DAVINCI_002("text-davinci-002"),
    UNKNOWN("UNKNOWN");

    @Getter
    private final String value;

    private static final List<AIModel> ENUM_LIST = new ArrayList<>();

    static {
        ENUM_LIST.addAll(EnumSet.allOf(GPT3Model.class));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static GPT3Model findByValue(String value) {
        return (GPT3Model) AIModel.fromValue(value, ENUM_LIST, UNKNOWN);
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

}
