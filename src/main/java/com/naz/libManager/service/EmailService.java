package com.naz.libManager.service;

public interface EmailService {
    void sendMail(String message, String subject, String recipient);
}
