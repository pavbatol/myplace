package ru.pavbatol.myplace.gateway.app.config.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Value("${app.http.connect-timeout:5s}")
    Duration connectTimeout;
    @Value("${app.http.read-timeout:10s}")
    Duration readTimeout;

    /**
     * @deprecated This method is deprecated in favor of {@link WebClientConfig#defaultWebClient()} webClient()}.
     */
    @Bean
    @Primary
    @Deprecated(since = "1.0.0.0", forRemoval = true)
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .build();
    }

    /**
     * @deprecated Use {@link WebClientConfig#defaultWebClient()} instead.
     */
    @Bean
    @Qualifier("longOperationTemplate")
    @Deprecated(since = "1.0.0.0", forRemoval = true)
    public RestTemplate longOperationRestTemplate(RestTemplateBuilder builder) {
        return builder
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofMinutes(3))
                .build();
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }
}
