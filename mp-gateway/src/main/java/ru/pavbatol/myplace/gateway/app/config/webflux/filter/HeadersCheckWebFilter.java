package ru.pavbatol.myplace.gateway.app.config.webflux.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.servlet.HandlerMapping;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.gateway.app.annotation.RequiredHeaders;
import ru.pavbatol.myplace.gateway.app.exeption.MissingHeaderException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reactive WebFilter that validates required headers specified in {@link RequiredHeaders} annotation.
 *
 * <p>Transition note: Part of reactive stack migration, may temporarily coexist with Servlet-based
 * implementations during transition period: {@link ru.pavbatol.myplace.gateway.app.aspect.HeadersCheckAspect}
 *
 * <p>Operation flow:
 * <ol>
 *   <li>Intercepts incoming requests
 *   <li>Checks for presence of all headers specified in {@code @RequiredHeaders}
 *   <li>Rejects requests with missing required headers (HTTP 400)
 *   <li>Propagates valid requests downstream
 * </ol>
 *
 * @see RequiredHeaders
 */
@Slf4j
@Order(2)
@Component
public class HeadersCheckWebFilter implements WebFilter {

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        log.debug("Headers validation started");

        Object handlerObj = exchange.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (handlerObj == null) {
            log.error("Header validation: Aborted (attribute HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE not found)");
            return Mono.error(new IllegalStateException("Attribute BEST_MATCHING_HANDLER_ATTRIBUTE not found"));
        }
        if (!(handlerObj instanceof HandlerMethod)) {
            log.debug("Header validation: OK (no headers required)");
            return chain.filter(exchange);
        }

        HandlerMethod handlerMethod = (HandlerMethod) handlerObj;
        RequiredHeaders requiredHeaders = handlerMethod.getMethodAnnotation(RequiredHeaders.class);

        if (requiredHeaders == null) {
            log.debug("Header validation: OK (no headers required)");
            return chain.filter(exchange);
        }

        List<String> missing = Arrays.stream(requiredHeaders.value())
                .filter(headerName -> exchange.getRequest().getHeaders().getFirst(headerName) == null)
                .collect(Collectors.toList());

        if (!missing.isEmpty()) {
            log.error("Header validation: FAILED (missing={})", missing);
            throw new MissingHeaderException(missing);
        }

        log.debug("Header validation: OK (required={})", Arrays.toString(requiredHeaders.value()));

        return chain.filter(exchange);
    }
}
