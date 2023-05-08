package dev.struchkov.openai.domain.message;

import dev.struchkov.openai.domain.response.Usage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnswerMessage {

    private String message;
    private Usage usage;

}