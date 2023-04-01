package dev.struchkov.quarkus.openai.client.provider;

import dev.struchkov.openai.domain.response.error.GptErrorDetail;
import dev.struchkov.openai.domain.response.error.GptErrorResponse;
import dev.struchkov.openai.exception.gpt.OpenAIGptApiException;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.ws.rs.core.Response;

public class ExceptionMapper implements ResponseExceptionMapper<Exception> {

    @Override
    public Exception toThrowable(Response r) {
        final GptErrorDetail errorDetail = r.readEntity(GptErrorResponse.class).getError();
        return new OpenAIGptApiException(
                errorDetail.getMessage(),
                errorDetail.getType(),
                errorDetail.getParam(),
                errorDetail.getCode()
        );
    }

}
