package dev.struchkov.openai.domain.model.gpt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GPT3Model implements GPTModel {

    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_3_5_TURBO_0301("gpt-3.5-turbo-0301"),
    TEXT_DAVINCI_003("text-davinci-003"),
    TEXT_DAVINCI_002("text-davinci-002");

    private final String value;

}
