package ru.pavbatol.myplace.dto.view;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class ViewDtoAddRequest {
    @NotBlank
    String app;

    @NotBlank
    String uri;

    @NotBlank
    String ip;

    @NotNull
    LocalDateTime timestamp;
}
