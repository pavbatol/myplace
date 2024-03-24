package ru.pavbatol.myplace.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import ru.pavbatol.myplace.dto.annotation.CustomJsonDateTimeFormat;

import java.time.LocalDateTime;

@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ViewDtoAddResponse {
    String id;
    String app;
    String uri;
    String ip;
    @CustomJsonDateTimeFormat
    LocalDateTime timestamp;
}
