package dev.struchkov.quarkus.openai.impl;

import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.request.picture.PictureRequest;
import dev.struchkov.openai.domain.response.PictureData;
import dev.struchkov.openai.quarkus.context.GPTClient;
import dev.struchkov.openai.quarkus.context.service.ImageGptService;
import dev.struchkov.quarkus.openai.BaseGptService;
import io.smallrye.mutiny.Uni;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

import static dev.struchkov.openai.domain.request.format.impl.PictureResponseFormat.URL;

public class ImageGptServiceImpl extends BaseGptService implements ImageGptService {

    public ImageGptServiceImpl(GPTClient client) {
        super(client);
    }

    @Override
    public Uni<List<String>> generateImage(PictureRequest request) {
        final GptRequest gptRequest = GptRequest.builder()
                .n(request.getNumberOfImages())
                .prompt(request.getDescription())
                .size(request.getSize())
                .responseFormat(request.getPictureResponseFormat())
                .user(request.getUser())
                .build();
        return client.executePicture(gptRequest)
                .map(resp -> Objects.isNull(request.getPictureResponseFormat()) || URL.equals(request.getPictureResponseFormat()) ?
                        resp.getData().stream().map(PictureData::getUrl).toList() :
                        resp.getData().stream().map(PictureData::getB64_json).toList()
                );
    }

}
