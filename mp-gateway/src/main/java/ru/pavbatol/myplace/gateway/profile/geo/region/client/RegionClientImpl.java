package ru.pavbatol.myplace.gateway.profile.geo.region.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.profile.geo.region.RegionDto;

import java.util.HashMap;
import java.util.Map;

@Component
public class RegionClientImpl extends BaseRestClient implements RegionClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/regions";
    private static final String PRIVATE_CONTEXT = "/user/geo/regions";

    public RegionClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                            RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    @Override
    public ResponseEntity<Object> create(RegionDto dto, HttpHeaders headers) {
        return post(ADMIN_CONTEXT, headers, dto);
    }

    @Override
    public ResponseEntity<Object> update(Long regionId, RegionDto dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s", regionId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    @Override
    public ResponseEntity<Object> delete(Long regionId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", regionId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> getById(Long regionId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", regionId);
        String fullResourcePath = PRIVATE_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> getAll(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size, HttpHeaders headers) {
        String paramsPath = "?" +
                "nameStartWith={nameStartWith}" +
                "&lastSeenName={lastSeenName}" +
                "&lastSeenCountryName={lastSeenCountryName}" +
                "&size={size}";
        String fullResourcePath = PRIVATE_CONTEXT + paramsPath;

        Map<String, Object> params = new HashMap<>(8);
        params.put("nameStartWith", nameStartWith);
        params.put("lastSeenName", lastSeenName);
        params.put("lastSeenCountryName", lastSeenCountryName);
        params.put("size", size);

        return get(fullResourcePath, headers, params);
    }
}
