package com.duikt.malachite.command;

import com.duikt.malachite.event.MessageEvent;
import com.duikt.malachite.keyboard.KeyboardService;
import com.duikt.malachite.localization.LocalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final ApplicationEventPublisher eventPublisher;
    private final LocalizationService localizationService;
    private final KeyboardService keyboardService;

    @Override
    public boolean canHandle(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        return update.getMessage().getText().equals(CommandName.START.getName());
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(localizationService.getLocalizationMessage(chatId, "menu.welcome"))
                .replyMarkup(keyboardService.getMainMenuKeyboard(chatId))
                .build();

        eventPublisher.publishEvent(new MessageEvent(this, message));
    }

    @Override
    public String getCommand() {
        return CommandName.START.getName();
    }
}
