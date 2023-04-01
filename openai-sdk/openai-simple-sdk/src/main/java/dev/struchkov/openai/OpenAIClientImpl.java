package dev.struchkov.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.struchkov.openai.context.OpenAIClient;
import dev.struchkov.openai.domain.conf.GPTConfig;
import dev.struchkov.openai.domain.response.account.TotalUsageResponse;
import dev.struchkov.openai.domain.response.error.GptErrorDetail;
import dev.struchkov.openai.domain.response.error.GptErrorResponse;
import dev.struchkov.openai.exception.OpenAIApiException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

@Slf4j
@RequiredArgsConstructor
public class OpenAIClientImpl implements OpenAIClient {

    private final ObjectMapper mapper;
    private final OkHttpClient okHttpClient;
    private final GPTConfig gptConfig;

    public OpenAIClientImpl(GPTConfig gptConfig) {
        this.mapper = new ObjectMapper();
        this.gptConfig = gptConfig;
        this.okHttpClient = new OkHttpClient();
    }

    @Override
    public TotalUsageResponse getTotalUsageInThisMount() {
        final LocalDate today = LocalDate.now();
        LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1);
        YearMonth currentMonth = YearMonth.from(today);
        YearMonth nextMonth = currentMonth.plusMonths(1);
        LocalDate firstDayOfNextMonth = nextMonth.atDay(1);

        final Request request = generateRequestBuilder(
                generateHttpUrlBuilder()
                        .addQueryParameter("start_date", firstDayOfCurrentMonth.toString())
                        .addQueryParameter("end_date", firstDayOfNextMonth.toString())
                        .build()
        ).get().build();

        try {
            final Response response = okHttpClient.newCall(request).execute();
            final String responseJson = response.body().string();
            if (response.isSuccessful()) {
                return mapper.readValue(responseJson, TotalUsageResponse.class);
            } else {
                final GptErrorResponse errorResponse = mapper.readValue(responseJson, GptErrorResponse.class);
                final GptErrorDetail errorDetail = errorResponse.getError();
                throw new OpenAIApiException(
                        errorDetail.getMessage(),
                        errorDetail.getType(),
                        errorDetail.getParam(),
                        errorDetail.getCode()
                );
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @SneakyThrows
    @NotNull
    private Request.Builder generateRequestBuilder(HttpUrl httpUrl) {
        return new Request.Builder()
                .url(httpUrl)
                .header("Authorization", "Bearer " + gptConfig.getToken());
    }

    private HttpUrl.Builder generateHttpUrlBuilder() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.openai.com")
                .addEncodedPathSegment("dashboard")
                .addEncodedPathSegment("billing")
                .addEncodedPathSegment("usage");
    }

}
