package ru.pavbatol.myplace.dto.view;

import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ViewDtoResponse {
    @With
    String app;
    @With
    String uri;
    @With
    Long views;
}
