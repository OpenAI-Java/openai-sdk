package dev.struchkov.quarkus.openai.impl;

import dev.struchkov.openai.domain.request.audio.VoiceToTextRequest;
import dev.struchkov.openai.quarkus.context.GPTClient;
import dev.struchkov.openai.quarkus.context.service.AudioGptService;
import dev.struchkov.quarkus.openai.BaseGptService;
import io.smallrye.mutiny.Uni;

public class AudioServiceImpl extends BaseGptService implements AudioGptService {

    public AudioServiceImpl(GPTClient client) {
        super(client);
    }

    @Override
    public Uni<String> voiceToSpeech(VoiceToTextRequest request) {
//        final GptRequest gptRequest = GptRequest.builder()
//                .model(request.getModel())
//                .file(request.getFile())
//                .prompt(request.getPrompt())
//                .responseFormat(request.getResponseFormat())
//                .temperature(request.getTemperature())
//                .language(request.getLanguage())
//                .build();
//        return client.executeVoiceToText(gptReauest)
//                .map(GptResponse::getText);
        return Uni.createFrom().failure(new RuntimeException("not implemented"));
    }

}
