package dev.struchkov.quarkus.openai.impl.chatgpt;

import com.github.f4b6a3.uuid.UuidCreator;
import dev.struchkov.openai.domain.chat.CreateMainChat;
import dev.struchkov.openai.domain.chat.MainChatInfo;
import dev.struchkov.openai.quarkus.context.GPTClient;
import dev.struchkov.openai.quarkus.context.data.MainChatGptStorage;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import static dev.struchkov.haiti.utils.Checker.checkNotNull;

@Slf4j
public class DefaultChatGptServiceImpl extends AbstractChatGptService<MainChatInfo, CreateMainChat> {

    public DefaultChatGptServiceImpl(
            GPTClient client,
            MainChatGptStorage<MainChatInfo> chatStorage
    ) {
        super(client, chatStorage);
    }

    @Override
    public Uni<MainChatInfo> createChat(CreateMainChat createChat) {
        final MainChatInfo mainChatInfo = new MainChatInfo();
        mainChatInfo.setChatId(checkNotNull(createChat.getChatId()) ? createChat.getChatId() : UuidCreator.getTimeOrderedEpochPlus1());
        mainChatInfo.setTemperature(checkNotNull(createChat.getTemperature()) ? createChat.getTemperature() : 1.0);
        mainChatInfo.setUserId(createChat.getUserId());
        mainChatInfo.setContextConstraint(createChat.getContextConstraint());
        mainChatInfo.setSystemBehavior(createChat.getSystemBehavior());
        mainChatInfo.setGptModel(createChat.getGptModel());
        return chatStorage.save(mainChatInfo)
                .invoke(chatInfo -> log.debug("Был создан новый чат: {}", chatInfo));
    }

    @Override
    public Uni<MainChatInfo> updateChat(MainChatInfo updateChat) {
        return chatStorage.findChatInfoById(updateChat.getChatId())
                .flatMap(existChatInfo -> {
                    existChatInfo.setSystemBehavior(updateChat.getSystemBehavior());
                    existChatInfo.setContextConstraint(updateChat.getContextConstraint());
                    existChatInfo.setTemperature(updateChat.getTemperature());
                    existChatInfo.setGptModel(updateChat.getGptModel());
                    return chatStorage.save(existChatInfo);
                });
    }

}
