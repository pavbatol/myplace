package ru.pavbatol.myplace.security.email.service;


import ru.pavbatol.myplace.security.app.exception.SendingMailException;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text) throws SendingMailException;
}
