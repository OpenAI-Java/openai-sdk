package dev.struchkov.openai.domain.response.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TotalUsageResponse {

    private String object;

    @JsonProperty("total_usage")
    private Double totalUsage;

}
