package dev.struchkov.openai;

import dev.struchkov.openai.query.HttpQueryCallback;

public interface HttpQueryExecutor {

    ReactiveHttpClient getHttpClient();

    default  <T> T call(HttpQueryCallback<T> callback) {
        return callback.execute(getHttpClient());
    }

}
