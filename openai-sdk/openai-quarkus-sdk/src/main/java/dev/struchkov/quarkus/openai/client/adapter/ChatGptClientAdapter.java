package dev.struchkov.quarkus.openai.client.adapter;

import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.request.MultipartGptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import dev.struchkov.openai.quarkus.context.GPTClient;
import dev.struchkov.quarkus.openai.client.gen.GptRestClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ChatGptClientAdapter implements GPTClient {

    private final GptRestClient client;

    @Override
    public Uni<GptResponse> executeChat(@NonNull GptRequest request) {
        log.trace("Получен запрос к ChatGPT: {}", request);
        return client.getChatCompletion(request)
                .invoke(response -> log.trace("Получен ответ отChatGpt: {}", response));
    }

    @Override
    public Multi<GptResponse> executeChatStream(@NonNull GptRequest request) {
        log.trace("Получен стрим запрос к ChatGPT: {}", request);
        return client.getChatCompletionStream(request)
                .skip().first()
                .skip().last(2)
                .map(json -> Json.decodeValue(json, GptResponse.class));
    }

    @Override
    public Uni<GptResponse> executePicture(@NonNull GptRequest request) {
        log.trace("Получен запрос к DALL-E: {}", request);
        return client.getGeneratedImage(request);
    }

    @Override
    public Uni<GptResponse> executePictureVariations(@NonNull MultipartGptRequest request) {
        log.trace("Получен запрос к DALL-E: {}", request);
        return client.getImageVariations(request);
    }

    @Override
    public Uni<GptResponse> executePictureEdits(@NonNull MultipartGptRequest request) {
        log.trace("Получен запрос к DALL-E: {}", request);
        return client.getImageEdits(request);
    }

//    @Override
//    public Uni<GptResponse> executeVoiceToText(@NonNull MultipartGptRequest request) {
//        return client.getTranscription(request);
//    }

}
