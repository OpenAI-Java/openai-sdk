package dev.struchkov.quarkus.openai.client.gen;

import dev.struchkov.quarkus.openai.client.provider.ExceptionMapper;
import dev.struchkov.quarkus.openai.client.provider.RequestInterceptor;
import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@RegisterProvider(ExceptionMapper.class)
@RegisterProvider(RequestInterceptor.class)
@RegisterRestClient(baseUri = "https://api.openai.com")
@ClientHeaderParam(name = "Authorization", value = "${auth.header}")
@ClientHeaderParam(name = "OpenAI-Organization", value = "${org.header}", required = false)
public interface GptRestClient {

    @POST
    @Path("/chat/completions")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<GptResponse> getChatCompletion(GptRequest request);

    @POST
    @Path("/chat/completions")
    @Produces(RestMediaType.APPLICATION_STREAM_JSON)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<String> getChatCompletionStream(GptRequest request);

    @POST
    @Path("/images/generations")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<GptResponse> getGeneratedImage(GptRequest request);

}
