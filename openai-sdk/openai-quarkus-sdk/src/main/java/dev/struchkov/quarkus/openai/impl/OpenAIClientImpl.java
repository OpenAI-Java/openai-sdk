package dev.struchkov.quarkus.openai.impl;

import dev.struchkov.openai.domain.response.account.TotalUsageResponse;
import dev.struchkov.openai.quarkus.context.OpenAIClient;
import dev.struchkov.quarkus.openai.client.gen.OpenAIRestClient;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@RequiredArgsConstructor
public class OpenAIClientImpl implements OpenAIClient {

    private final OpenAIRestClient openAIRestClient;

    @Override
    public Uni<TotalUsageResponse> getTotalUsageInThisMount() {
        final LocalDate today = LocalDate.now();
        LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1);
        YearMonth currentMonth = YearMonth.from(today);
        YearMonth nextMonth = currentMonth.plusMonths(1);
        LocalDate firstDayOfNextMonth = nextMonth.atDay(1);
        return openAIRestClient.getTotalUsageTokens(firstDayOfCurrentMonth.toString(), firstDayOfNextMonth.toString());
    }

}
