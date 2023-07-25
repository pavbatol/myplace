package ru.pavbatol.myplace.dto.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import ru.pavbatol.myplace.dto.annotation.CustomJsonDateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ViewDtoAddRequest {
    @NotBlank
    String app;

    @NotBlank
    String uri;

    @NotBlank
    String ip;

    @CustomJsonDateTimeFormat
    LocalDateTime timestamp;
}
