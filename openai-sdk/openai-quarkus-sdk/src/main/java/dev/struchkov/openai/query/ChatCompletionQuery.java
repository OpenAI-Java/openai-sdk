package dev.struchkov.openai.query;

import dev.struchkov.openai.ReactiveHttpClient;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "from")
public class ChatCompletionQuery implements HttpQueryCallback<Uni<GptResponse>> {

    private final GptRequest request;

    @Override
    public Uni<GptResponse> execute(ReactiveHttpClient httpClient) {
        return httpClient.postAsync("/v1/chat/completions", request)
                .map(resp -> resp.body().toJsonObject().mapTo(GptResponse.class));
    }

}
