package dev.struchkov.quarkus.openai.impl;

import dev.struchkov.openai.domain.request.GptRequest;
import dev.struchkov.openai.domain.request.MultipartGptRequest;
import dev.struchkov.openai.domain.request.picture.PictureRequest;
import dev.struchkov.openai.domain.response.GptResponse;
import dev.struchkov.openai.domain.response.PictureData;
import dev.struchkov.openai.quarkus.context.GPTClient;
import dev.struchkov.openai.quarkus.context.service.ImageGptService;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

import static dev.struchkov.openai.domain.request.format.impl.PictureResponseFormat.URL;

@RequiredArgsConstructor
public class ImageGptServiceImpl implements ImageGptService {

    protected final GPTClient client;

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
                .map(resp -> generateResponse(resp, request));
    }

    @Override
    public Uni<List<String>> generateImageVariations(PictureRequest request) {
        final MultipartGptRequest gptRequest = MultipartGptRequest.builder()
                .n(request.getNumberOfImages())
                .size(request.getSize().toString())
                .responseFormat(request.getPictureResponseFormat().toString())
                .user(request.getUser())
                .image(request.getImage())
                .build();
        return client.executePictureVariations(gptRequest)
                .map(resp -> generateResponse(resp, request));
    }

    @Override
    public Uni<List<String>> generateImageEdits(PictureRequest request) {
        final MultipartGptRequest gptRequest = MultipartGptRequest.builder()
                .n(request.getNumberOfImages())
                .size(request.getSize().toString())
                .prompt(request.getDescription())
                .responseFormat(request.getPictureResponseFormat().toString())
                .user(request.getUser())
                .image(request.getImage())
                .mask(request.getMask())
                .build();
        return client.executePictureEdits(gptRequest)
                .map(resp -> generateResponse(resp, request));
    }

    private List<String> generateResponse(GptResponse response, PictureRequest pictureRequest) {
        return Objects.isNull(pictureRequest.getPictureResponseFormat()) || URL.equals(pictureRequest.getPictureResponseFormat()) ?
                response.getData().stream().map(PictureData::getUrl).toList() :
                response.getData().stream().map(PictureData::getB64_json).toList();
    }

}
