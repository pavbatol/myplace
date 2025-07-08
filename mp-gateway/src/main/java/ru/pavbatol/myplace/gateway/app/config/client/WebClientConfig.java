package ru.pavbatol.myplace.gateway.app.config.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import ru.pavbatol.myplace.shared.dto.api.ApiError;
import ru.pavbatol.myplace.shared.exception.TargetServiceErrorException;
import ru.pavbatol.myplace.shared.exception.TargetServiceHandledErrorException;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    @Value("${app.http.connect-timeout:5s}")
    private Duration connectTimeout;
    @Value("${app.http.read-timeout:10s}")
    private Duration readTimeout;

    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, WebClient> cachedClients = new ConcurrentHashMap<>();

    /**
     * Provides a pre-configured {@link WebClient.Builder} as a Spring Bean.
     * <p>
     * This builder can later be enhanced with Spring Cloud's {@code @LoadBalanced} annotation
     * to enable client-side load balancing when calling other services.
     * </p>
     *
     * @return a standard WebClient builder instance
     * @see "org.springframework.cloud.client.loadbalancer.LoadBalanced"
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient defaultWebClient() {
        return webClientBuilder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(connectTimeout.plus(readTimeout))
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                                        (int) connectTimeout.toMillis()
                                )
                ))
                .filter(errorHandlingFilter())
                .build();
    }

    @Bean
    public Function<String, WebClient> webClientFactory() {
        return this::getMutatedWebClient;
    }

    private WebClient getMutatedWebClient(String baseUrl) {
        return cachedClients.computeIfAbsent(
                baseUrl,
                url -> {
                    if (url == null || url.isBlank()) {
                        throw new IllegalArgumentException("Base URL must not be null or empty");
                    }
                    log.info("Mutate WebClient with base url: {}", url);

                    return defaultWebClient().mutate()
                            .baseUrl(url)
                            .build();
                }
        );
    }

    private ExchangeFilterFunction errorHandlingFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (response.statusCode().isError()) {
                return response.bodyToMono(String.class)
                        .defaultIfEmpty("No error details provided")
                        .flatMap(errorBody -> {
                            try {
                                return Mono.error(new TargetServiceHandledErrorException(
                                        objectMapper.readValue(errorBody, ApiError.class),
                                        response.statusCode()
                                ));
                            } catch (Exception e) {
                                return Mono.error(new TargetServiceErrorException(
                                        ApiError.message(errorBody, response.statusCode().toString()),
                                        response.statusCode()
                                ));
                            }
                        });
            }

            return Mono.just(response);
        });
    }
}
