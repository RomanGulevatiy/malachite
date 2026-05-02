package com.duikt.malachite.command;

import com.duikt.malachite.event.MessageEvent;
import com.duikt.malachite.localization.LocalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class AboutCommand implements Command {

    private final ApplicationEventPublisher eventPublisher;
    private final LocalizationService localizationService;

    @Override
    public boolean canHandle(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        String localizationMessage = localizationService.getLocalizationMessage(
                update.getMessage().getChatId(),
                "menu.about");
        return update.getMessage().getText().equalsIgnoreCase(localizationMessage);
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();

        String localizationMessage = localizationService.getLocalizationMessage(
                chatId,
                "system.about");

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(localizationMessage)
                .build();

        eventPublisher.publishEvent(new MessageEvent(this, message));
    }

    @Override
    public String getCommand() {
        return CommandName.ABOUT.getName();
    }
}
