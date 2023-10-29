package ru.pavbatol.myplace.email.service;


import ru.pavbatol.myplace.app.exception.SendingMailException;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text) throws SendingMailException;
}
