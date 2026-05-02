package com.duikt.malachite.command;

import com.duikt.malachite.event.MessageEvent;
import com.duikt.malachite.localization.LocalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LanguageCommand implements Command {

    public static final String LANGUAGE_EN = "language_en";
    public static final String LANGUAGE_UA = "language_ua";

    private final ApplicationEventPublisher eventPublisher;
    private final LocalizationService localizationService;

    @Override
    public boolean canHandle(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        String localizationMessage = localizationService.getLocalizationMessage(
                update.getMessage().getChatId(),
                "menu.language");
        return update.getMessage().getText().equalsIgnoreCase(localizationMessage);
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();

        String localizationMessage = localizationService.getLocalizationMessage(
                chatId,
                "language.select");

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(localizationMessage)
                .replyMarkup(languageKeyboard(chatId))
                .build();

        eventPublisher.publishEvent(new MessageEvent(this, message));
    }

    private ReplyKeyboard languageKeyboard(Long chatId) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizationMessage(chatId, "language.english"))
                .callbackData(LANGUAGE_EN)
                .build())
        );
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizationMessage(chatId, "language.ukrainian"))
                .callbackData(LANGUAGE_UA)
                .build())
        );

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public String getCommand() {
        return CommandName.LANGUAGE.getName();
    }
}
