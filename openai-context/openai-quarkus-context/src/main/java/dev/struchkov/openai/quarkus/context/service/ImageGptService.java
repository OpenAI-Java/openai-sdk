package dev.struchkov.openai.quarkus.context.service;

import dev.struchkov.openai.domain.request.picture.PictureRequest;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface ImageGptService {

    Uni<List<String>> generateImage(PictureRequest request);

}
