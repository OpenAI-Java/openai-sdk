package dev.struchkov.quarkus.openai.impl.chatgpt;

import dev.struchkov.openai.domain.common.GptMessage;
import dev.struchkov.openai.domain.message.AnswerMessage;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.Choice;
import dev.struchkov.openai.quarkus.context.GPTClient;
import dev.struchkov.openai.quarkus.context.service.InstructGPTService;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InstructGPTServiceImpl implements InstructGPTService {

    private final GPTClient client;

    @Override
    public Uni<AnswerMessage> execute(String gptModel, String message) {
        return client.executeChat(
                GptRequest.builder()
                        .model(gptModel)
                        .messages(List.of(GptMessage.fromUser(message)))
                        .build()
        ).map(gptResponse -> {
            final List<Choice> choices = gptResponse.getChoices();
            final String gptAnswer = choices.get(choices.size() - 1).getMessage().getContent();
            return AnswerMessage.builder()
                    .message(gptAnswer)
                    .usage(gptResponse.getUsage())
                    .build();
        });
    }

}
