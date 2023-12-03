package dev.struchkov.openai.quarkus.context.service;

import dev.struchkov.openai.domain.message.AnswerMessage;
import io.smallrye.mutiny.Uni;

public interface InstructGPTService {

    Uni<AnswerMessage> execute(String aiModel, String message);

}
