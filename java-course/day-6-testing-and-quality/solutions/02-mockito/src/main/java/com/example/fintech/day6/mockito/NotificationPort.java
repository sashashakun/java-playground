package com.example.fintech.day6.mockito;

/** Sends notifications to users (email, push, SMS). */
public interface NotificationPort {
    void notify(String userId, String subject, String body);
}
