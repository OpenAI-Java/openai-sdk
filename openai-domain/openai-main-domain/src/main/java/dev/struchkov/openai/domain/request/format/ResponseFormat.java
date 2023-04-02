package dev.struchkov.openai.domain.request.format;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.struchkov.openai.domain.deser.FormatDeserializer;

@JsonDeserialize(using = FormatDeserializer.class)
public interface ResponseFormat {

    String getValue();

}
