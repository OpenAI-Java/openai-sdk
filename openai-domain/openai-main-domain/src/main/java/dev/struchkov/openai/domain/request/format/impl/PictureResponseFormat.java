package dev.struchkov.openai.domain.request.format.impl;

import com.fasterxml.jackson.annotation.JsonValue;
import dev.struchkov.openai.domain.request.format.ResponseFormat;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PictureResponseFormat implements ResponseFormat {

    URL("url"),
    B64_JSON("b64_json");

    private final String value;

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @Override
    public String getValue() {
        return value;
    }
}

