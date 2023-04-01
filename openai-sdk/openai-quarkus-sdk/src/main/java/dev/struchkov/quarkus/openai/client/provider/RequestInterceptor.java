package dev.struchkov.quarkus.openai.client.provider;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

@Slf4j
public class RequestInterceptor implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext){
        log.info("Request entity: %s".formatted(requestContext.getEntity()));
        log.info("Headers: %s".formatted(requestContext.getHeaders().entrySet()));
    }

}
