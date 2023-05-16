package dev.struchkov.openai.domain.request;


import lombok.Builder;
import lombok.Getter;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import jakarta.ws.rs.core.MediaType;

@Getter
@Builder
public class MultipartGptRequest {

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    private String prompt;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    private Integer n;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    private String user;

    @RestForm
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] image;

    @RestForm
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] mask;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    private String size;

    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    private String model;

    @RestForm("response_format")
    @PartType(MediaType.TEXT_PLAIN)
    private String responseFormat;


//    @RestForm
//    @PartType("audio/mpeg")
//    private byte[] file;
//
//    @RestForm
//    @PartType(MediaType.TEXT_PLAIN)
//    private Double temperature;
//
//    @RestForm
//    @PartType(MediaType.TEXT_PLAIN)
//    private String language;

}
