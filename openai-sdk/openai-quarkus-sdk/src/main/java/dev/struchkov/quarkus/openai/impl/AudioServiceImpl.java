package dev.struchkov.quarkus.openai.impl;

import dev.struchkov.openai.domain.request.audio.VoiceToTextRequest;
import dev.struchkov.openai.quarkus.context.GPTClient;
import dev.struchkov.openai.quarkus.context.service.AudioGptService;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AudioServiceImpl implements AudioGptService {

    protected final GPTClient client;

    @Override
    public Uni<String> voiceToSpeech(VoiceToTextRequest request) {
//        final MultipartGptRequest gptRequest = MultipartGptRequest.builder()
//                .model(request.getModel().getValue())
//                .file(request.getFile())
//                .prompt(request.getPrompt())
//                .responseFormat(request.getResponseFormat().getValue())
//                .temperature(request.getTemperature())
//                .language(request.getLanguage())
//                .build();
//        return client.executeVoiceToText(gptRequest)
//                .map(GptResponse::getText);
        return Uni.createFrom().failure(new RuntimeException("not implemented"));
    }

}
