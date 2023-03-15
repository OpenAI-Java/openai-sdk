package dev.struchkov.openai.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.struchkov.openai.domain.model.AIModel;
import dev.struchkov.openai.domain.model.gpt.GPT3Model;
import dev.struchkov.openai.domain.model.gpt.GPT4Model;

import java.lang.reflect.Type;
import java.util.Optional;

public class AIModelGsonSer implements JsonSerializer<AIModel>, JsonDeserializer<AIModel> {

    @Override
    public JsonElement serialize(AIModel aiModel, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(aiModel.getValue());
    }

    @Override
    public AIModel deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            final Optional<GPT3Model> optGpt3 = GPT3Model.findByValue(json.getAsString());
            if (optGpt3.isPresent()) {
                return optGpt3.get();
            }

            final Optional<GPT4Model> optGpt4 = GPT4Model.findByValue(json.getAsString());
            if (optGpt4.isPresent()) {
                return optGpt4.get();
            }
            throw new JsonParseException("Invalid value for enum MyEnum: " + json.getAsString());
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Invalid value for enum MyEnum: " + json.getAsString());
        }
    }

}
