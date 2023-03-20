package dev.struchkov.openai.service;

import dev.struchkov.openai.ReactiveHttpClient;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import dev.struchkov.openai.query.ChatCompletionQuery;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import lombok.Getter;
import lombok.NonNull;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ChatGptClientServiceImpl implements ChatGptClientService {

    @Getter
    private final ReactiveHttpClient httpClient;


    public ChatGptClientServiceImpl(
            Vertx vertx,
            @ConfigProperty(name = "gpt.token") String gptToken,
            @ConfigProperty(name = "gpt.organization") Optional<String> organization
    ) {
        httpClient = new ReactiveHttpClient(
                vertx,
                Map.of(
                        "Authorization", "Bearer %s".formatted(gptToken),
                        "OpenAI-Organization", organization.orElse("")
                ));
    }

    @Override
    public Uni<GptResponse> getChatCompletion(@NonNull GptRequest gptRequest) {
        return call(ChatCompletionQuery.from(gptRequest));
    }

}
