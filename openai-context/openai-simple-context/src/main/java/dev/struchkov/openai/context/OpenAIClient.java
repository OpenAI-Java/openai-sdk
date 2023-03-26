package dev.struchkov.openai.context;

import dev.struchkov.openai.domain.response.account.TotalUsageResponse;

public interface OpenAIClient {

    TotalUsageResponse getTotalUsageInThisMount();

}
