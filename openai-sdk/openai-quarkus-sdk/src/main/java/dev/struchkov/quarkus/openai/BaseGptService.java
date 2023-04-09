package dev.struchkov.quarkus.openai;

import dev.struchkov.openai.quarkus.context.GPTClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseGptService {

    protected final GPTClient client;

}

