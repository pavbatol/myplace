package ru.pavbatol.myplace.gateway.app.config.webmvc.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * <p>Custom {@link HttpServletRequestWrapper} that allows injecting additional headers into the request.
 * Maintains a map of custom headers that take precedence over original request headers.
 * <p>
 * Used in conjunction with {@link RequestWrappingFilter} for role-based access control scenarios.
 *
 * @deprecated This request wrapper is part of the servlet stack which is being phased out in favor of WebFlux.
 * During the transition period to reactive stack, this class remains operational but will be
 * removed once the migration to WebFlux is complete.
 */
@Deprecated(since = "1.0.0.0", forRemoval = true)
public class CustomHeaderRequestWrapper extends HttpServletRequestWrapper {
    private final Map<String, String> customHeaders = new HashMap<>();

    public CustomHeaderRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public void injectHeader(String name, String value) {
        customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        if (customHeaders.containsKey(name)) {
            return customHeaders.get(name);
        }

        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> names = new HashSet<>(customHeaders.keySet());
        Enumeration<String> original = super.getHeaderNames();
        while (original.hasMoreElements()) {
            names.add(original.nextElement());
        }

        return Collections.enumeration(names);
    }
}
