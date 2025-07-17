package ru.pavbatol.myplace.gateway.app.config.webmvc.filter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class servletConfig {

    @Bean
    public FilterRegistrationBean<RequestWrappingFilter> requestWrappingFilter(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        FilterRegistrationBean<RequestWrappingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestWrappingFilter(handlerMapping));
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);

        return registration;
    }
}
