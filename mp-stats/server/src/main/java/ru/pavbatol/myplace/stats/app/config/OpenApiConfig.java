package ru.pavbatol.myplace.stats.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Stats Api",
                description = "MyPlace, mp-stats service", version = "1.0.0",
                contact = @Contact(
                        name = "Sergey Pavlik",
                        email = "pavbatol@yandex.ru"
                )
        )
)
public class OpenApiConfig {
}
