package dev.struchkov.quarkus.openai.adapter;

import dev.struchkov.quarkus.openai.client.gen.GptRestClient;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import dev.struchkov.openai.quarkus.context.GPTClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import lombok.NonNull;
import org.eclipse.microprofile.rest.client.inject.RestClient;

public class ChatGptClientAdapter implements GPTClient {

    private final GptRestClient client;

    public ChatGptClientAdapter(@RestClient GptRestClient client) {
        this.client = client;
    }

    @Override
    public Uni<GptResponse> executeChat(@NonNull GptRequest request) {
        return client.getChatCompletion(request);
    }

    @Override
    public Multi<GptResponse> executeChatStream(@NonNull GptRequest request) {
        return client.getChatCompletionStream(request)
                .skip().first()
                .skip().last(2)
                .map(json -> Json.decodeValue(json, GptResponse.class));
    }

    @Override
    public Uni<GptResponse> executePicture(@NonNull GptRequest request) {
        return client.getGeneratedImage(request);
    }

}
