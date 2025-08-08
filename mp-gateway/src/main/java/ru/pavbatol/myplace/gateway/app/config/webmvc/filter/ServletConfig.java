package ru.pavbatol.myplace.gateway.app.config.webmvc.filter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * <p>Servlet configuration class that registers {@link RequestWrappingFilter} as a bean with lowest precedence.
 * The filter is configured with {@link RequestMappingHandlerMapping} to support role-based request processing.
 * <p>
 * Note: The filter order is set to {@link Ordered#LOWEST_PRECEDENCE} to ensure it runs after other filters.
 *
 * @deprecated This configuration is part of the servlet stack which is being phased out in favor of WebFlux.
 * During the transition period to reactive stack, this class remains operational but will be
 * removed once the migration to WebFlux is complete.
 */
@Deprecated(since = "1.0.0.0", forRemoval = true)
@Configuration
public class ServletConfig {

    @Bean
    public FilterRegistrationBean<RequestWrappingFilter> requestWrappingFilter(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        FilterRegistrationBean<RequestWrappingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestWrappingFilter(handlerMapping));
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);

        return registration;
    }
}
