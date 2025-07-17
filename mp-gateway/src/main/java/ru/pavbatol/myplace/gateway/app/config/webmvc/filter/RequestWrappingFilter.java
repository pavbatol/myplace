package ru.pavbatol.myplace.gateway.app.config.webmvc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ServletRequestPathUtils;
import ru.pavbatol.myplace.gateway.app.access.RequiredRoles;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
