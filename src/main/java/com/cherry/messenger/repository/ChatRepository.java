package com.cherry.messenger.repository;

import com.cherry.messenger.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}