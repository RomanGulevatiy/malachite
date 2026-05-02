package com.duikt.malachite.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class BotEventListener {

    private final TelegramClient telegramClient;

    @EventListener
    public void onMessageEvent(MessageEvent event) {
        try {
            telegramClient.execute(event.getMessage());
        }
        catch(TelegramApiException e) {
            log.error("Failed to send message: {}", e.getMessage());
        }
    }
}
