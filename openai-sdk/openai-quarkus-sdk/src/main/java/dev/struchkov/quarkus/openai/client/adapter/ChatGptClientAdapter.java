package dev.struchkov.quarkus.openai.client.adapter;

import dev.struchkov.quarkus.openai.client.gen.GptRestClient;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import dev.struchkov.openai.quarkus.context.GPTClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class ChatGptClientAdapter implements GPTClient {

    private final GptRestClient client;

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

//    @Override
//    public Uni<GptResponse> executeVoiceToText(@NonNull GptRequest gptRequest) {
//        return client.getTranscription(gptRequest);
//    }

}
