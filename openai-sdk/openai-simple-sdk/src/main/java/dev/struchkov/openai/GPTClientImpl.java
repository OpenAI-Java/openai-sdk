package dev.struchkov.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.struchkov.openai.context.GPTClient;
import dev.struchkov.openai.domain.conf.GPTConfig;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import dev.struchkov.openai.domain.response.error.GptErrorDetail;
import dev.struchkov.openai.domain.response.error.GptErrorResponse;
import dev.struchkov.openai.exception.gpt.OpenAIGptApiException;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static dev.struchkov.haiti.utils.Checker.checkNotBlank;

@Slf4j
public class GPTClientImpl implements GPTClient {

    private static final MediaType MEDIA_TYPE_APPLICATION_JSON = MediaType.parse("application/json");

    private final ObjectMapper mapper;
    private final OkHttpClient okHttpClient;
    private final GPTConfig gptConfig;

    public GPTClientImpl(GPTConfig gptConfig) {
        this.mapper = new ObjectMapper();
        this.gptConfig = gptConfig;
        final Duration timeout = Duration.ofMinutes(1L);
        this.okHttpClient = new OkHttpClient.Builder()
                .callTimeout(timeout)
                .writeTimeout(timeout)
                .connectTimeout(timeout)
                .readTimeout(timeout)
                .build();
    }

    public GPTClientImpl(ObjectMapper mapper, OkHttpClient okHttpClient, GPTConfig gptConfig) {
        this.mapper = mapper;
        this.okHttpClient = okHttpClient;
        this.gptConfig = gptConfig;
    }

    @Override
    public CompletableFuture<GptResponse> executeAsync(@NonNull GptRequest gptRequest) {
        final CompletableFuture<GptResponse> future = new CompletableFuture<>();
        final Request request = generateRequest(gptRequest);
        okHttpClient.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        future.completeExceptionally(e);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String responseJson = response.body().string();
                        if (response.isSuccessful()) {
                            future.complete(mapper.readValue(responseJson, GptResponse.class));
                        } else {
                            final GptErrorResponse errorResponse = mapper.readValue(responseJson, GptErrorResponse.class);
                            final GptErrorDetail errorDetail = errorResponse.getError();
                            future.completeExceptionally(new OpenAIGptApiException(
                                    errorDetail.getMessage(),
                                    errorDetail.getType(),
                                    errorDetail.getParam(),
                                    errorDetail.getCode()
                            ));
                        }
                    }
                }
        );
        return future;
    }

    @Override
    public GptResponse execute(@NonNull GptRequest gptRequest) {
        try {
            final Request request = generateRequest(gptRequest);
            final Response response = okHttpClient.newCall(request).execute();
            final String responseJson = response.body().string();
            if (response.isSuccessful()) {
                return mapper.readValue(responseJson, GptResponse.class);
            } else {
                final GptErrorResponse errorResponse = mapper.readValue(responseJson, GptErrorResponse.class);
                final GptErrorDetail errorDetail = errorResponse.getError();
                throw new OpenAIGptApiException(
                        errorDetail.getMessage(),
                        errorDetail.getType(),
                        errorDetail.getParam(),
                        errorDetail.getCode()
                );
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        throw new RuntimeException();
    }

    @SneakyThrows
    @NotNull
    private Request generateRequest(@NotNull GptRequest gptRequest) {
        final RequestBody body = RequestBody.create(MEDIA_TYPE_APPLICATION_JSON, mapper.writeValueAsString(gptRequest));

        final Request.Builder requestBuilder = new Request.Builder()
                .url(gptConfig.getUrl() + "/v1/chat/completions")
                .header("Authorization", "Bearer " + gptConfig.getToken())
                .post(body);

        final String openAiOrg = gptConfig.getOrganisation();
        if (checkNotBlank(openAiOrg)) {
            requestBuilder.header("OpenAI-Organization", openAiOrg);
        }
        return requestBuilder.build();
    }

}
