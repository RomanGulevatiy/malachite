package com.duikt.malachite.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    public UserSession getOrCreateUserSession(Long chatId) {
        Optional<UserSession> userSession = userSessionRepository.findByChatId(chatId);

        return userSession.orElseGet(() -> {
            log.info("Creating new user session for chatId: {}", chatId);
            UserSession newUserSession = new UserSession();
            newUserSession.setChatId(chatId);
            newUserSession.setLocale("en");
            return userSessionRepository.save(newUserSession);
        });
    }

    public Locale getLocale(Long chatId) {
        return Locale.forLanguageTag(getOrCreateUserSession(chatId).getLocale());
    }

    @Transactional
    public void setLocale(Long chatId, String locale) {
        UserSession userSession = getOrCreateUserSession(chatId);
        userSession.setLocale(locale);
        userSessionRepository.save(userSession);
    }
}
