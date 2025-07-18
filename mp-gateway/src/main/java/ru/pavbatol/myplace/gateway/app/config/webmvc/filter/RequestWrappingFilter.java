package ru.pavbatol.myplace.gateway.app.config.webmvc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ServletRequestPathUtils;
import ru.pavbatol.myplace.gateway.app.annotation.RequiredRoles;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Filter that wraps HTTP requests when handler methods are annotated with {@link RequiredRoles}.
 * Parses and caches request path if not already present. Wraps the original request
 * with {@link CustomHeaderRequestWrapper} for methods requiring role-based access control.
 * <p>
 * Extends {@link OncePerRequestFilter} to ensure single execution per request.
 *
 * @deprecated This filter is part of the servlet stack which is being phased out in favor of WebFlux.
 * During the transition period to reactive stack, this class remains operational but will be
 * removed once the migration to WebFlux is complete.
 */
@Deprecated(since = "1.0.0.0", forRemoval = true)
@Slf4j
public class RequestWrappingFilter extends OncePerRequestFilter {
    @Qualifier("requestMappingHandlerMapping")
    private final RequestMappingHandlerMapping handlerMapping;

    public RequestWrappingFilter(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws IOException, ServletException {
        if (request.getAttribute(ServletRequestPathUtils.PATH_ATTRIBUTE) == null) {
            ServletRequestPathUtils.parseAndCache(request);
            log.debug("Request path parsed: {}", ServletRequestPathUtils.getParsedRequestPath(request));
        }

        try {
            HandlerExecutionChain handler = handlerMapping.getHandler(request);

            if (handler != null && handler.getHandler() instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler.getHandler();

                if (handlerMethod.hasMethodAnnotation(RequiredRoles.class)) {
                    request = new CustomHeaderRequestWrapper(request);
                    log.debug("Wrapping request. Method: {}, Annotation: {}",
                            handlerMethod.getMethod().getName(),
                            handlerMethod.getMethodAnnotation(RequiredRoles.class));

                    log.debug("Request class after wrapping: {}", request.getClass().getName());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to check RequiredRoles annotation for {}: {}", request.getRequestURI(), e.getMessage());
        }

        chain.doFilter(request, response);
    }
}
