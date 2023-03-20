package dev.struchkov.openai.query;


import dev.struchkov.openai.ReactiveHttpClient;

@FunctionalInterface
public interface HttpQueryCallback<T> {

    T execute(ReactiveHttpClient httpClient);

}
