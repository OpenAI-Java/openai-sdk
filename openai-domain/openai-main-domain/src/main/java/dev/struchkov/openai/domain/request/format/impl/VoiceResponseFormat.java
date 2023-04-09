package dev.struchkov.openai.domain.request.format.impl;

import com.fasterxml.jackson.annotation.JsonValue;
import dev.struchkov.openai.domain.request.format.ResponseFormat;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VoiceResponseFormat implements ResponseFormat {

    JSON("json"),
    TEXT("text"),
    SRT("srt"),
    VERBOSE_JSON("verbose_json"),
    VTT("btt");

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
