package ru.pavbatol.myplace.stats.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final String serverDev = "localhost";
    @Value("${server.port}")
    private String port;

    @Bean
    public OpenAPI generateOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Stats service Api")
                                .description("MyPlace >> mp-stats service")
                                .contact(new Contact()
                                        .name("Sergey Pavlik")
                                        .email("pavbatol@yandex.ru")
                                )
                                .license(new License()
                                        .name("License MIT")
                                        .url("https://mit-license.org/")
                                )
                                .version("1.0.0")
                )
                .servers(
                        List.of(
                                new Server()
                                        .url(String.format("%s:%s", serverDev, port))
                                        .description("develop")
                        )
                );
    }
}
