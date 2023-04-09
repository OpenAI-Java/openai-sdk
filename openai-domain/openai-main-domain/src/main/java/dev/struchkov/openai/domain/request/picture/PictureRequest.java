package dev.struchkov.openai.domain.request.picture;

import dev.struchkov.openai.domain.request.format.impl.PictureResponseFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class PictureRequest {

    @NonNull
    private String description;

    private Integer numberOfImages;

    private PictureSize size;

    private PictureResponseFormat pictureResponseFormat;

    private String user;

}

