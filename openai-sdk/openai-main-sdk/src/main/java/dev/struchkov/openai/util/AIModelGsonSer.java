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

public class AIModelGsonSer implements JsonSerializer<AIModel>, JsonDeserializer<AIModel> {

    @Override
    public JsonElement serialize(AIModel aiModel, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(aiModel.getValue());
    }

    @Override
    public AIModel deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            final GPT3Model optGpt3 = GPT3Model.findByValue(json.getAsString());
            if (!optGpt3.equals(GPT3Model.UNKNOWN)) {
                return optGpt3;
            }

            final GPT4Model optGpt4 = GPT4Model.findByValue(json.getAsString());
            if (!optGpt4.equals(GPT4Model.UNKNOWN)) {
                return optGpt4;
            }
            throw new JsonParseException("Invalid value for enum MyEnum: " + json.getAsString());
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Invalid value for enum MyEnum: " + json.getAsString());
        }
    }

}
