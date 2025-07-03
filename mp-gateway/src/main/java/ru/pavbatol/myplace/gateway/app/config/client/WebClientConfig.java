package ru.pavbatol.myplace.gateway.app.config.client;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {
    @Value("${app.http.connect-timeout:5s}")
    private Duration connectTimeout;
    @Value("${app.http.read-timeout:10s}")
    private Duration readTimeout;

    @Bean
    @Primary
    public WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(connectTimeout.plus(readTimeout))
                ))
                .build();
    }

    @Bean
    @Qualifier("longTimeoutWebClient")
    public WebClient longTimeoutWebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                                        Math.toIntExact(Duration.ofSeconds(30).toMillis())
                                )
                                .responseTimeout(Duration.ofMinutes(5))
                ))
                .build();
    }
}
