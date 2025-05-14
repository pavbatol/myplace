package ru.pavbatol.myplace.gateway.profile.geo.country.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;

import java.util.HashMap;
import java.util.Map;

@Component
public class CountryClientImpl extends BaseRestClient implements CountryClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/countries";
    private static final String PRIVATE_CONTEXT = "/user/geo/countries";

    public CountryClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                             RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    @Override
    public ResponseEntity<Object> create(CountryDto dto, HttpHeaders headers) {
        return post(ADMIN_CONTEXT, headers, dto);
    }

    @Override
    public ResponseEntity<Object> update(Long countryId, CountryDto dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s", countryId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    @Override
    public ResponseEntity<Object> delete(Long countryId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", countryId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> getById(Long countryId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", countryId);
        String fullResourcePath = PRIVATE_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> getAll(String nameStartWith, int page, int size, HttpHeaders headers) {
        String paramsPath ="?" +
                        "nameStartWith={nameStartWith}" +
                        "&page={page}" +
                        "&size={size}";
        String fullResourcePath = PRIVATE_CONTEXT + paramsPath;

        Map<String, Object> params = new HashMap<>(4);
        params.put("nameStartWith", nameStartWith);
        params.put("page", page);
        params.put("size", size);

        return get(fullResourcePath, headers, params);
    }
}
