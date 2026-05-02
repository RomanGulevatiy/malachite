package com.duikt.malachite.callback;

import com.duikt.malachite.command.Command;
import com.duikt.malachite.command.CommandName;
import com.duikt.malachite.command.LanguageCommand;
import com.duikt.malachite.event.MessageEvent;
import com.duikt.malachite.keyboard.KeyboardService;
import com.duikt.malachite.localization.LocalizationService;
import com.duikt.malachite.user.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class LanguageCallbackCommand implements Command {

    private final ApplicationEventPublisher eventPublisher;
    private final LocalizationService localizationService;
    private final UserSessionService userSessionService;
    private final KeyboardService keyboardService;

    @Override
    public boolean canHandle(Update update) {
        if(!update.hasCallbackQuery() || update.getCallbackQuery().getData() == null) {
            return false;
        }
        
        String callbackData = update.getCallbackQuery().getData();
        return callbackData.equals(LanguageCommand.LANGUAGE_EN)
                || callbackData.equals(LanguageCommand.LANGUAGE_UA);
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String callbackData = update.getCallbackQuery().getData();

        String languageCode = callbackData.equals(LanguageCommand.LANGUAGE_EN) ? "en" : "ua";
        userSessionService.setLocale(chatId, languageCode);

        String localizationMessage = localizationService.getLocalizationMessage(
                chatId,
                "language.switched");

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(keyboardService.getMainMenuKeyboard(chatId))
                .text(localizationMessage)
                .build();
        eventPublisher.publishEvent(new MessageEvent(this, message));
    }

    @Override
    public String getCommand() {
        return CommandName.LANGUAGE.getName();
    }
}
