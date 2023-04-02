package dev.struchkov.openai.quarkus.context.service;

import dev.struchkov.openai.domain.request.audio.VoiceToTextRequest;
import io.smallrye.mutiny.Uni;

public interface AudioGptService {

    Uni<String> voiceToSpeech(VoiceToTextRequest request);

}
