package dev.struchkov.quarkus.openai.client.gen;

import dev.struchkov.openai.domain.request.MultipartGptRequest;
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

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/v1")
@RegisterProvider(ExceptionMapper.class)
@RegisterProvider(RequestInterceptor.class)
@RegisterRestClient(baseUri = "https://api.openai.com")
@ClientHeaderParam(name = "Authorization", value = "${openai.token}")
//@ClientHeaderParam(name = "OpenAI-Organization", value = "${openai.organisation}", required = false)
public interface GptRestClient {

    @POST
    @Path("/completions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<GptResponse> getCompletion(GptRequest request);

    @POST
    @Path("/chat/completions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<GptResponse> getChatCompletion(GptRequest request);

    @POST
    @Path("/chat/completions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(RestMediaType.APPLICATION_STREAM_JSON)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    Multi<String> getChatCompletionStream(GptRequest request);

    @POST
    @Path("/images/generations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<GptResponse> getGeneratedImage(GptRequest request);

    @POST
    @Path("/images/variations")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<GptResponse> getImageVariations(MultipartGptRequest request);

    @POST
    @Path("/images/edits")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<GptResponse> getImageEdits(MultipartGptRequest request);

    // вероятно проблема на стороне api, он не хавает форматы, которые подходят из списка и все равно пишет, что формат не тот
    // https://community.openai.com/t/whisper-api-cannot-read-files-correctly/93420/37
//    @POST
//    @Path("/audio/transcriptions")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MediaType.APPLICATION_JSON)
//    Uni<GptResponse> getTranscription(MultipartGptRequest request);

}
