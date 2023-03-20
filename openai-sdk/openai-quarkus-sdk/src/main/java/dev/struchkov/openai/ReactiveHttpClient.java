package dev.struchkov.openai;

import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.error.GptErrorDetail;
import dev.struchkov.openai.domain.response.error.GptErrorResponse;
import dev.struchkov.openai.exception.gpt.OpenAIGptApiException;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.impl.headers.HeadersMultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.core.http.HttpClient;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ReactiveHttpClient {

    private final HttpClient httpClient;

    private final WebClient webClient;

    private final Map<String, String> headers;

    public ReactiveHttpClient(Vertx vertx, Map<String, String> headers) {
        this.httpClient = vertx.createHttpClient(new HttpClientOptions()
                .setTrustAll(true)
                .setVerifyHost(false)
                .setSsl(true)
                .setConnectTimeout(10_000)
                .setDefaultHost("api.openai.com")
                .setDefaultPort(443));
        this.webClient = WebClient.wrap(httpClient);
        this.headers = headers;
    }

    public Uni<HttpResponse<Buffer>> postAsync(String requestURI, GptRequest request) {
        final JsonObject requestBody = JsonObject.mapFrom(request);
        log.info("Request: %s".formatted(requestBody));
        return webClient
                .post(requestURI)
                .putHeaders(headers())
                .putHeader("Accept", "application/json")
                .sendJson(requestBody)
                .invoke(resp -> log.info("Response: %s".formatted(resp.body())))
                .map(Unchecked.function(resp -> {
                    if (resp.statusCode() > 299) {
                        final GptErrorDetail errorDetail = resp.body().toJsonObject().mapTo(GptErrorResponse.class).getError();
                        throw new OpenAIGptApiException(
                                errorDetail.getMessage(),
                                errorDetail.getType(),
                                errorDetail.getParam(),
                                errorDetail.getCode()
                        );
                    }
                    return resp;
                }));
    }

    private MultiMap headers() {
        MultiMap multiMap = MultiMap.newInstance(new HeadersMultiMap());
        headers.forEach(multiMap::set);
        return multiMap;
    }

}
