package ru.pavbatol.myplace.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import ru.pavbatol.myplace.app.annotation.ExcludeJacocoGenerated;

// TODO: 13.09.2023  Remove 'ExcludeJacocoGenerated' annotation when developing this service

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
@ExcludeJacocoGenerated
public class OpenApiConfig {
}
