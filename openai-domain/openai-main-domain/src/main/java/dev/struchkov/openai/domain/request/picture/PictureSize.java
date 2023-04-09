package dev.struchkov.openai.domain.request.picture;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PictureSize {

    SMALL("256x256", 0.016d),
    MEDIUM("512x512", 0.018d),
    BIG("1024x1024", 0.020d);

    private final String value;

    // price per image depends on size requested
    private final double price;

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

}
