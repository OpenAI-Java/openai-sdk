package dev.struchkov.quarkus.openai.client.provider;

import lombok.extern.slf4j.Slf4j;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;

@Slf4j
public class RequestInterceptor implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext){
        log.trace("Request entity: %s".formatted(requestContext.getEntity()));
        log.trace("Headers: %s".formatted(requestContext.getHeaders().entrySet()));
    }

}
