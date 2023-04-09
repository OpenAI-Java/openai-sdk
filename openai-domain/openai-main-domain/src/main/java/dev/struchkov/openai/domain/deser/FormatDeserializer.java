package dev.struchkov.openai.domain.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.struchkov.openai.domain.request.format.impl.PictureResponseFormat;
import dev.struchkov.openai.domain.request.format.ResponseFormat;
import dev.struchkov.openai.domain.request.format.impl.VoiceResponseFormat;

import java.io.IOException;
import java.util.stream.Stream;

public class FormatDeserializer extends StdDeserializer<ResponseFormat> {

    public FormatDeserializer() {
        super(ResponseFormat.class);
    }

    @Override
    public ResponseFormat deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        final String value = ctx.readValue(p, String.class);
        return Stream.of(PictureResponseFormat.values(), VoiceResponseFormat.values())
                .flatMap(Stream::of)
                .filter(model -> model.getValue().equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value for enum: %s".formatted(value)));
    }

}
