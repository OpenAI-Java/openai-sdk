package dev.struchkov.openai.quarkus.context;

import dev.struchkov.openai.domain.response.account.TotalUsageResponse;
import io.smallrye.mutiny.Uni;

public interface OpenAIClient {

    Uni<TotalUsageResponse> getTotalUsageInThisMount();

}
