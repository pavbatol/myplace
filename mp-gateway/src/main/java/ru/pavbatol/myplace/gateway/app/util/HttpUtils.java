package ru.pavbatol.myplace.gateway.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpUtils {

    public static HttpHeaders extractHeaders(@NonNull HttpServletRequest httpServletRequest) {
        HttpHeaders headers = new HttpHeaders();
        Collections.list(httpServletRequest.getHeaderNames())
                .forEach(headerName -> headers.set(headerName, httpServletRequest.getHeader(headerName)));
        return headers;
    }

    public static void ensureDefaultHeaders(@NonNull HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<MediaType> acceptHeaders = new ArrayList<>(headers.getAccept());
        acceptHeaders.remove(MediaType.APPLICATION_JSON);
        acceptHeaders.add(0, MediaType.APPLICATION_JSON);

        headers.setAccept(acceptHeaders);
    }

    public static HttpHeaders prepareHeadersWithDefaults(@NonNull HttpServletRequest httpServletRequest) {
        HttpHeaders headers = extractHeaders(httpServletRequest);
        ensureDefaultHeaders(headers);

        return headers;
    }
}
