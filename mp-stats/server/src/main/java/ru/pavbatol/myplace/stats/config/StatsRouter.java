package ru.pavbatol.myplace.stats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class StatsRouter {
    @Bean
    public RouterFunction<ServerResponse> route(StatsHandler statsHandler){
        return RouterFunctions
                .route(
                        GET("/stats/{id:[0-9]+}")
                                .and(accept(APPLICATION_JSON)), statsHandler::getId)
                .andRoute(
                        GET("/stats")
                                .and(accept(APPLICATION_JSON)), statsHandler::getByName);
    }
}
