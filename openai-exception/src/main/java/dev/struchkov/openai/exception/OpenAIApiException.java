package dev.struchkov.openai.exception;

import dev.struchkov.haiti.context.exception.BasicException;

import java.text.MessageFormat;

public class OpenAIApiException extends BasicException {

    public OpenAIApiException() {
        super(null);
    }

    public OpenAIApiException(String message) {
        super(message);
    }

    public OpenAIApiException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public OpenAIApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
