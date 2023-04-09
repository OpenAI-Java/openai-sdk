package dev.struchkov.quarkus.openai;

import dev.struchkov.openai.quarkus.context.GPTClient;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class BaseGptService {

    protected final GPTClient client;

}

