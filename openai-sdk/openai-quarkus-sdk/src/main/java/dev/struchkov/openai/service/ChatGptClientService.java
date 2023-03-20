package dev.struchkov.openai.service;

import dev.struchkov.openai.HttpQueryExecutor;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;

public interface ChatGptClientService extends HttpQueryExecutor {

    Uni<GptResponse> getChatCompletion(@NonNull GptRequest gptRequest);

}
