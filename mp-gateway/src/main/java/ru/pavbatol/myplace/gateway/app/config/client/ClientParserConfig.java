package ru.pavbatol.myplace.gateway.app.config.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.pavbatol.myplace.shared.client.ResponseBodyParser;

@Configuration
public class ClientParserConfig {

    @Bean
    public ResponseBodyParser responseBodyParser(ObjectMapper objectMapper) {
        return new ResponseBodyParser(objectMapper);
    }
}
