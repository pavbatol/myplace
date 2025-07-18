package ru.pavbatol.myplace.gateway.app.config.webflux.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.servlet.HandlerMapping;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.gateway.app.annotation.RequiredRoles;
import ru.pavbatol.myplace.shared.constant.HttpHeaders;
import ru.pavbatol.myplace.shared.exception.TargetServiceErrorException;

import java.util.List;
import java.util.function.Function;

/**
 * Reactive WebFilter for handling {@link RequiredRoles} annotated methods. Verifies permissions
 * via Security service call and optionally injects user ID/UUID headers into the request.
 *
 * <p>Transition note: While migrating to reactive stack, some Servlet-based components
 * ({@link ru.pavbatol.myplace.gateway.app.access.AccessCheckAspect}) might still coexist during the transition period.
 *
 * <p>Operation flow:
 * <ol>
 *   <li>Intercepts requests to {@code @RequiredRoles} methods
 *   <li>Requests permission check via {@value #CHECK_ACCESS_PATH}
 *   <li>Processes response (success/denial)
 *   <li>Injects user headers (if configured in annotation)
 * </ol>
 */
@Slf4j
@Order(1)
@Component
public class AccessCheckWebFilter implements WebFilter {
    private static final String CHECK_ACCESS_PATH = "/permission/check-access";
    private final WebClient securityClient;

    public AccessCheckWebFilter(@Value("${app.mp.security.url}") String serverUrl,
                                @Autowired Function<String, WebClient> webClientFactory) {
        this.securityClient = webClientFactory.apply(serverUrl);
    }

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        log.debug("Access check started");

        Object handlerObj = exchange.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (handlerObj == null) {
            log.error("Access check: Aborted (attribute HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE not found)");
            return Mono.error(new IllegalStateException("Attribute BEST_MATCHING_HANDLER_ATTRIBUTE not found"));
        }
        if (!(handlerObj instanceof HandlerMethod)) {
            log.debug("Access check: OK (no roles required)");
            return chain.filter(exchange);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handlerObj;
        RequiredRoles rolesAnnotation = handlerMethod.getMethodAnnotation(RequiredRoles.class);

        if (rolesAnnotation == null) {
            log.debug("Access check: OK (no roles required)");
            return chain.filter(exchange);
        }

        String authToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization token"));
        }

        String userAgent = exchange.getRequest().getHeaders().getFirst(HttpHeaders.USER_AGENT);

        log.debug("Sending request to Security service for checking access");
        return securityClient.post()
                .uri("/permission/check-access")
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .bodyValue(List.of(rolesAnnotation.roles()))
                .exchangeToMono(response -> {
                    if (!rolesAnnotation.setUserIdHeader() && !rolesAnnotation.setUserUuidHeader()) {
                        log.debug("Access check: OK (granted, no headers to inject)");
                        return chain.filter(exchange);
                    }

                    ServerHttpRequest mutatedRequest = processHeaders(exchange.getRequest(), response, rolesAnnotation);
                    log.debug("Access check: OK (granted)");
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                })
                .onErrorResume(throwable -> {
                    int statusCode = 500;
                    if (throwable instanceof WebClientResponseException) {
                        statusCode = ((WebClientResponseException) throwable).getStatusCode().value();
                    } else if (throwable instanceof ResponseStatusException) {
                        statusCode = ((ResponseStatusException) throwable).getStatus().value();
                    } else if (throwable instanceof TargetServiceErrorException) {
                        statusCode = ((TargetServiceErrorException) throwable).getStatus().value();
                    }

                    log.error("Access check: FAILED; HTTP Status: {}", statusCode, throwable);
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.FORBIDDEN, "Access denied. Original status: " + statusCode));
                });
    }

    private ServerHttpRequest processHeaders(final ServerHttpRequest originalRequest,
                                             final ClientResponse response,
                                             final RequiredRoles rolesAnnotation) {
        ServerHttpRequest.Builder requestBuilder = originalRequest.mutate();

        processHeader(requestBuilder, response, rolesAnnotation.setUserIdHeader(), rolesAnnotation.userIdHeader(), "User ID");
        processHeader(requestBuilder, response, rolesAnnotation.setUserUuidHeader(), rolesAnnotation.userUuidHeader(), "User UUID");

        return requestBuilder.build();
    }

    private void processHeader(final ServerHttpRequest.Builder requestBuilder,
                               final ClientResponse response,
                               final boolean shouldProcess,
                               final String headerName,
                               final String headerDescription) {
        if (shouldProcess) {
            List<String> headerValues = response.headers().header(headerName);
            if (headerValues.isEmpty()) {
                throw new RuntimeException(headerDescription + " not obtained from response headers");
            }
            requestBuilder.headers(headers -> headers.set(headerName, headerValues.get(0)));
        }
    }
}
