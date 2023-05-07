package dev.struchkov.openai.domain.request.audio;

import dev.struchkov.openai.domain.model.gpt.Whisper;
import dev.struchkov.openai.domain.request.format.impl.VoiceResponseFormat;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class VoiceToTextRequest {

    private byte[] file;

    private Whisper model;

    private String prompt;

    private VoiceResponseFormat responseFormat;

    private Double temperature;

    private String language;

}
