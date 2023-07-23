package ru.pavbatol.myplace.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ViewDtoResponse {
    String app;
    String uri;
    Long views;
}
