package dev.struchkov.openai.domain.model;

import java.util.List;

public interface AIModel {

    String getValue();

    static AIModel fromValue(String value, List<AIModel> enumList, AIModel unknownVal) {
        return enumList.stream().filter(enumItem ->
                        value.equalsIgnoreCase(enumItem.getValue()))
                .findAny().orElse(unknownVal);
    }

}
