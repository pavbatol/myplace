package ru.pavbatol.myplace.gateway.app.exeption;

import java.util.List;

public class MissingHeaderException extends RuntimeException {
    public MissingHeaderException(List<String> headerNames) {
        super("Required header missing: " + headerNames);
    }
}
