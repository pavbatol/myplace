package ru.pavbatol.myplace.shared.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A builder for creating {@link HttpHeaders} with optional default settings.
 * <p>
 * By default, the builder applies the following headers:
 * <ul>
 *     <li>{@code Content-Type: application/json}</li>
 *     <li>{@code Accept: application/json}</li>
 * </ul>
 * If the {@code Accept} header already contains other values, {@code application/json} will be
 * prioritized and placed at the beginning of the list.
 * <p>
 * These defaults can be disabled using the {@link #disableDefaults()} method.
 * <p>
 * Additional headers can be added using the {@code with*} methods, such as
 * {@link #withUserUuid(UUID)}, {@link #withUserId(Long)}, and {@link #withHeader(String, String)}.
 * <p>
 * Example usage:
 * <pre>
 * HttpHeaders headers = HttpHeadersBuilder.create()
 *     .withUserUuid(UUID.randomUUID())
 *     .withUserId(123L)
 *     .withHeader("Custom-Header", "Value")
 *     .build();
 * </pre>
 */
public final class HttpHeadersBuilder {
    private static final String X_USER_UUID = "X-User-Uuid";
    private static final String X_USER_ID = "X-User-Id";
    private final HttpHeaders headers;
    private boolean useDefaults = true;

    private HttpHeadersBuilder(HttpHeaders headers) {
        this.headers = new HttpHeaders();
        this.headers.putAll(headers);
    }

    public static HttpHeadersBuilder create() {
        return new HttpHeadersBuilder(new HttpHeaders());
    }

    public static HttpHeadersBuilder create(@NotNull HttpHeaders headers) {
        return new HttpHeadersBuilder(headers);
    }

    /**
     * Disables the application of default headers.
     * <p>
     * By default, the builder applies the following headers:
     * <ul>
     *     <li>{@code Content-Type: application/json}</li>
     *     <li>{@code Accept: application/json}</li>
     * </ul>
     * Calling this method will prevent these defaults from being applied when
     * {@link #build()} is called.
     * <p>
     * Example usage:
     * <pre>
     * HttpHeaders headers = HttpHeadersBuilder.create()
     *     .disableDefaults()
     *     .withUserUuid(UUID.randomUUID())
     *     .withUserId(123L)
     *     .build();
     * </pre>
     *
     * @return This builder instance for method chaining.
     */
    public HttpHeadersBuilder disableDefaults() {
        this.useDefaults = false;
        return this;
    }

    public HttpHeadersBuilder withUserUuid(@Nullable UUID userUuid) {
        if (userUuid != null) {
            headers.set(X_USER_UUID, userUuid.toString());
        }
        return this;
    }

    public HttpHeadersBuilder withUserId(@Nullable Long userId) {
        if (userId != null) {
            headers.set(X_USER_ID, String.valueOf(userId));
        }
        return this;
    }

    public HttpHeadersBuilder withHeader(String headerName, String headerValue) {
        headers.set(headerName, headerValue);
        return this;
    }

    /**
     * Builds the {@link HttpHeaders} instance with the configured headers.
     * <p>
     * If defaults are enabled (default behavior), the following headers will be applied:
     * <ul>
     *     <li>{@code Content-Type: application/json}</li>
     *     <li>{@code Accept: application/json}</li>
     * </ul>
     * If the {@code Accept} header already contains other values, {@code application/json} will be
     * prioritized and placed at the beginning of the list.
     * <p>
     * Example usage:
     * <pre>
     * HttpHeaders headers = HttpHeadersBuilder.create()
     *     .withUserUuid(UUID.randomUUID())
     *     .withUserId(123L)
     *     .build();
     * </pre>
     *
     * @return A new {@link HttpHeaders} instance with the configured headers.
     */
    public HttpHeaders build() {
        if (useDefaults) {
            headers.setContentType(MediaType.APPLICATION_JSON);

            List<MediaType> acceptHeaders = new ArrayList<>(headers.getAccept());
            acceptHeaders.remove(MediaType.APPLICATION_JSON);
            acceptHeaders.add(0, MediaType.APPLICATION_JSON);

            headers.setAccept(acceptHeaders);
        }

        HttpHeaders result = new HttpHeaders();
        result.putAll(headers);

        return result;
    }
}
