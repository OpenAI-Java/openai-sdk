package dev.struchkov.openai;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.struchkov.openai.context.GPTClient;
import dev.struchkov.openai.domain.conf.GPTConfig;
import dev.struchkov.openai.domain.model.gpt.GPT3Model;
import dev.struchkov.openai.domain.model.gpt.GPT4Model;
import dev.struchkov.openai.domain.model.gpt.GPTModel;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import dev.struchkov.openai.domain.response.error.GptErrorDetail;
import dev.struchkov.openai.domain.response.error.GptErrorResponse;
import dev.struchkov.openai.exception.gpt.OpenAIGptApiException;
import dev.struchkov.openai.util.AIModelGsonSer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Duration;

import static dev.struchkov.haiti.utils.Checker.checkNotBlank;

@Slf4j
public class GPTClientImpl implements GPTClient {

    private static final MediaType MEDIA_TYPE_APPLICATION_JSON = MediaType.parse("application/json");

    private final Gson gson;

    {
        final AIModelGsonSer aiModelGsonSer = new AIModelGsonSer();
        gson = new GsonBuilder()
                .registerTypeAdapter(GPT3Model.class, aiModelGsonSer)
                .registerTypeAdapter(GPT4Model.class, aiModelGsonSer)
                .registerTypeAdapter(GPTModel.class, aiModelGsonSer)
                .create();
    }

    private final OkHttpClient okHttpClient;
    private final GPTConfig gptConfig;

    public GPTClientImpl(GPTConfig gptConfig) {
        this.gptConfig = gptConfig;
        final Duration timeout = Duration.ofMinutes(1L);
        this.okHttpClient = new OkHttpClient.Builder()
                .callTimeout(timeout)
                .writeTimeout(timeout)
                .connectTimeout(timeout)
                .readTimeout(timeout)
                .build();
    }

    @Override
    public GptResponse execute(@NonNull GptRequest gptRequest) {
        try {
            final Request request = generateRequest(gptRequest);
            final Response response = okHttpClient.newCall(request).execute();
            final String responseJson = response.body().string();
            if (response.isSuccessful()) {
                return gson.fromJson(responseJson, GptResponse.class);
            } else {
                final GptErrorResponse errorResponse = gson.fromJson(responseJson, GptErrorResponse.class);
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

    @NotNull
    private Request generateRequest(@NotNull GptRequest gptRequest) {
        final RequestBody body = RequestBody.create(MEDIA_TYPE_APPLICATION_JSON, gson.toJson(gptRequest));

        final Request.Builder requestBuilder = new Request.Builder()
                .url(gptConfig.getUrl())
                .header("Authorization", "Bearer " + gptConfig.getToken())
                .post(body);

        final String openAiOrg = gptConfig.getOrganisation();
        if (checkNotBlank(openAiOrg)) {
            requestBuilder.header("OpenAI-Organization", openAiOrg);
        }
        return requestBuilder.build();
    }

}
