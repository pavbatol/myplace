package ru.pavbatol.myplace.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Security service Api",
                description = "MyPlace >> mp-security service", version = "1.0.0",
                contact = @Contact(
                        name = "Pavlik Sergey",
                        email = "pavbatol@yandex.ru"
                ),
                license = @License(
                        name = "License MIT",
                        url = "https://mit-license.org/"
                )

        )
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class OpenApiConfig {
}
