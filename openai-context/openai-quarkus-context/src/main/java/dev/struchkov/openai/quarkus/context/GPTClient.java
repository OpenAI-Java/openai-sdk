package dev.struchkov.openai.quarkus.context;

import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;

public interface GPTClient {

    Uni<GptResponse> executeChat(@NonNull GptRequest gptRequest);
    Multi<GptResponse> executeChatStream(@NonNull GptRequest gptRequest);
    Uni<GptResponse> executePicture(@NonNull GptRequest gptRequest);

}