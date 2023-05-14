package dev.struchkov.openai.quarkus.context.service;

import dev.struchkov.openai.domain.message.AnswerMessage;
import dev.struchkov.openai.domain.model.gpt.GPTModel;
import io.smallrye.mutiny.Uni;

public interface InstructGPTService {

    Uni<AnswerMessage> execute(GPTModel aiModel, String message);

}
