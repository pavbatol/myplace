package ru.pavbatol.myplace.gateway.app.access;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.pavbatol.myplace.gateway.app.access.client.AccessClient;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessCheckAspect {
    public static final String AUTHORIZATION = "Authorization";
    private final AccessClient client;

    @Before("@annotation(requiredRoles)")
    public void checkAccess(RequiredRoles requiredRoles) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("Request context not found");
        }

        String authToken = attributes.getRequest().getHeader(AUTHORIZATION);
        if (authToken == null) {
            throw new IllegalStateException("Authorization header is missing");
        }

        try {
            client.checkAccess(List.of(requiredRoles.roles()), authToken);
        } catch (HttpStatusCodeException e) {
            throw new SecurityException("Access denied", e);
        }
    }
}