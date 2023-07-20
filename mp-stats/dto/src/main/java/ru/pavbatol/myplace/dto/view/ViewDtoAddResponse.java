package ru.pavbatol.myplace.dto.view;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ViewDtoAddResponse {
    String id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
