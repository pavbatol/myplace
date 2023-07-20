package ru.pavbatol.myplace.dto.view;

import lombok.Value;

@Value
public class ViewDtoResponse {
    String app;
    String uri;
    Long views;
}
