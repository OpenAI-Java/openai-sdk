package dev.struchkov.quarkus.openai.impl.chatgpt;

import dev.struchkov.openai.domain.common.GptMessage;
import dev.struchkov.openai.domain.message.AnswerMessage;
import dev.struchkov.openai.domain.model.gpt.GPT3Model;
import dev.struchkov.openai.domain.model.gpt.GPT4Model;
import dev.struchkov.openai.domain.model.gpt.GPTModel;
import dev.struchkov.openai.domain.model.gpt.InstructGPTModel;
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
    public Uni<AnswerMessage> execute(GPTModel gptModel, String message) {
        if (gptModel instanceof GPT3Model gpt3Model) {
            return client.executeChat(
                    GptRequest.builder()
                            .model(gpt3Model)
                            .messages(List.of(GptMessage.fromUser(message)))
                            .maxTokens(1000)
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
        if (gptModel instanceof GPT4Model gpt4Model) {
            return client.executeChat(
                    GptRequest.builder()
                            .model(gpt4Model)
                            .messages(List.of(GptMessage.fromUser(message)))
                            .maxTokens(1000)
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
        if (gptModel instanceof InstructGPTModel instructGPTModel) {
            return client.executeCompletion(
                    GptRequest.builder()
                            .model(instructGPTModel)
                            .prompt(message)
                            .maxTokens(1000)
                            .build()
            ).map(gptResponse -> {
                final List<Choice> choices = gptResponse.getChoices();
                final String gptAnswer = choices.get(choices.size() - 1).getText();
                return AnswerMessage.builder()
                        .message(gptAnswer)
                        .usage(gptResponse.getUsage())
                        .build();
            });
        }
        return Uni.createFrom().nullItem();
    }

}
