package dev.struchkov.openai.context;

import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import lombok.NonNull;

public interface GPTClient {

    GptResponse execute(@NonNull GptRequest gptRequest);

}
