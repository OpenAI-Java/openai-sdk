package dev.struchkov.openai.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dev.struchkov.openai.domain.model.AIModel;

import java.io.IOException;

public class AIModelGsonSer extends StdSerializer<AIModel> {

    public AIModelGsonSer() {
        this(null);
    }

    public AIModelGsonSer(Class<AIModel> t) {
        super(t);
    }

    @Override
    public void serialize(AIModel aiModel, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeString(aiModel.getValue());
        jsonGenerator.writeEndObject();
    }

}
