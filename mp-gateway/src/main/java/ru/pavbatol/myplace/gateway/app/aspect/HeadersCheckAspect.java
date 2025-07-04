package ru.pavbatol.myplace.gateway.app.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.pavbatol.myplace.gateway.app.annotation.RequiredHeaders;
import ru.pavbatol.myplace.gateway.app.exeption.MissingHeaderException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Order(2)
@Component
public class HeadersCheckAspect {

    @Before("@annotation(requiredHeaders)")
    public void validateHeaders(JoinPoint joinPoint, RequiredHeaders requiredHeaders) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        log.debug("Header validation started: Method: {}.{}() Invoked by: @{}",
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                RequiredHeaders.class.getSimpleName()
        );

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.error("Header validation aborted: ServletRequestAttributes not found in RequestContextHolder");
            throw new IllegalStateException("ServletRequestAttributes not available in current context");
        }

        HttpServletRequest request = attributes.getRequest();

        List<String> missing = Arrays.stream(requiredHeaders.value())
                .filter(header -> request.getHeader(header) == null)
                .collect(Collectors.toList());

        if (!missing.isEmpty()) {
            log.error("Header validation: FAILED (missing={})", missing);
            throw new MissingHeaderException(missing);
        }

        log.debug("Header validation: OK (required={})", Arrays.toString(requiredHeaders.value()));
    }
}
