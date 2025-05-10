package ru.pavbatol.myplace.gateway.app.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Configuration
public class RestTemplateConfig {
    @Value("${app.http.connect-timeout:5s}")
    Duration connectTimeout;
    @Value("${app.http.read-timeout:10s}")
    Duration readTimeout;

    private final ConcurrentHashMap<String, RestTemplate> templates = new ConcurrentHashMap<>();

    @Bean
    public Function<String, RestTemplate> restTemplateFactory(RestTemplateBuilder builder) {
        return serverUrl -> this.newRestTemplate(serverUrl, builder);
    }

    private RestTemplate newRestTemplate(String serverUrl, RestTemplateBuilder builder) {
        return templates.computeIfAbsent(serverUrl, url ->
                builder
                        .rootUri(url)
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .setConnectTimeout(connectTimeout)
                        .setReadTimeout(readTimeout)
                        .build());
    }
}
