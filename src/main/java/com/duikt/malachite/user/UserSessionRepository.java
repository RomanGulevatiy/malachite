package com.duikt.malachite.user;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends ListCrudRepository<UserSession, Long> {

    Optional<UserSession> findByChatId(Long chatId);
}
