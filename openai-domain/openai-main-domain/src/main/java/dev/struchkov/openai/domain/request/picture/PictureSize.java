package dev.struchkov.openai.domain.request.picture;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PictureSize {

    SMALL("256x256"),
    MEDIUM("512x512"),
    BIG("1024x1024");

    private final String value;

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
