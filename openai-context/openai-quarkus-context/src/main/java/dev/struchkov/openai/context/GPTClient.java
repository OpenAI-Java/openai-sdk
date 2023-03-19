package dev.struchkov.openai.context;

import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;

public interface GPTClient {

    Uni<GptResponse> execute(@NonNull GptRequest gptRequest);

}
