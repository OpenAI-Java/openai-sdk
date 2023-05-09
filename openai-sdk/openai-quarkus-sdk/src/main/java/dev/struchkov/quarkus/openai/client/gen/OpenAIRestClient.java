package dev.struchkov.quarkus.openai.client.gen;

import dev.struchkov.openai.domain.response.account.TotalUsageResponse;
import dev.struchkov.quarkus.openai.client.provider.ExceptionMapper;
import dev.struchkov.quarkus.openai.client.provider.RequestInterceptor;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/v1")
@RegisterProvider(ExceptionMapper.class)
@RegisterProvider(RequestInterceptor.class)
@RegisterRestClient(baseUri = "https://api.openai.com")
@ClientHeaderParam(name = "Authorization", value = "${openai.token}")
//@ClientHeaderParam(name = "OpenAI-Organization", value = "${openai.organisation}", required = false)
public interface OpenAIRestClient {

    @GET
    @Path("/dashboard/billing/usage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<TotalUsageResponse> getTotalUsageTokens(@QueryParam("start_date") String startDate, @QueryParam("end_date") String endDate);

}
