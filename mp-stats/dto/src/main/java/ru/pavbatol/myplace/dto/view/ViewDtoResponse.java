package ru.pavbatol.myplace.dto.view;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ViewDtoResponse {
    String app;
    String uri;
    Long views;
}
