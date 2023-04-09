package dev.struchkov.quarkus.openai.impl;

import dev.struchkov.openai.domain.request.audio.VoiceToTextRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import dev.struchkov.openai.quarkus.context.service.AudioGptService;
import dev.struchkov.quarkus.openai.BaseGptService;
import io.smallrye.mutiny.Uni;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class AudioServiceImpl extends BaseGptService implements AudioGptService {


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
