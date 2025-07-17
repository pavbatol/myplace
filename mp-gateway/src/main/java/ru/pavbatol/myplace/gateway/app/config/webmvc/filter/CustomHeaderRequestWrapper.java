package ru.pavbatol.myplace.gateway.app.config.webmvc.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

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
