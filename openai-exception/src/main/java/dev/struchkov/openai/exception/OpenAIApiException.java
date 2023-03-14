package dev.struchkov.openai.exception;

import dev.struchkov.haiti.context.exception.BasicException;

import java.text.MessageFormat;

public abstract class OpenAIApiException extends BasicException {

    protected OpenAIApiException() {
        super(null);
    }

    protected OpenAIApiException(String message) {
        super(message);
    }

    protected OpenAIApiException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    protected OpenAIApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
