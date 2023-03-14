package dev.struchkov.openai.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.struchkov.openai.domain.model.AIModel;

import java.lang.reflect.Type;

public class AIModelGsonSer implements JsonSerializer<AIModel> {

    @Override
    public JsonElement serialize(AIModel aiModel, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(aiModel.getValue());
    }

}
