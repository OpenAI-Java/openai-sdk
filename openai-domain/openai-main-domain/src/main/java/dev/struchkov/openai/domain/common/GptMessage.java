package dev.struchkov.openai.domain.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GptMessage {

    @Builder.Default
    private String role = "user";

    private String content;

    public static GptMessage fromUser(String content) {
        return GptMessage.builder().role("user").content(content).build();
    }

    public static GptMessage fromAssistant(String content) {
        return GptMessage.builder().role("assistant").content(content).build();
    }

    public static GptMessage fromSystem(String content) {
        return GptMessage.builder().role("system").content(content).build();
    }

}
