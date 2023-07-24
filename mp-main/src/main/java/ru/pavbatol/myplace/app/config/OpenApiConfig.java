package ru.pavbatol.myplace.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Main Api",
                description = "MyPlace, mp-main service", version = "1.0.0",
                contact = @Contact(
                        name = "Sergey Pavlik",
                        email = "pavbatol@yandex.ru"
                )
        )
)
public class OpenApiConfig {
}
