package ru.pavbatol.myplace.gateway.profile.geo.city.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.profile.geo.city.CityDto;

@Component
public class CityClientImpl extends BaseRestClient implements CityClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/cities";
    private static final String PRIVATE_CONTEXT = "/user/geo/cities";

    public CityClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                          RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    @Override
    public ResponseEntity<Object> create(CityDto dto, HttpHeaders headers) {
        return post(ADMIN_CONTEXT, headers, dto);
    }

    @Override
    public ResponseEntity<Object> update(Long cityId, CityDto dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s", cityId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    @Override
    public ResponseEntity<Object> delete(Long cityId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", cityId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> getById(Long cityId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", cityId);
        String fullResourcePath = PRIVATE_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(PRIVATE_CONTEXT)
                .queryParam("size", size);

        if (nameStartWith != null) {
            builder.queryParam("nameStartWith", nameStartWith);
        }
        if (lastSeenName != null) {
            builder.queryParam("lastSeenName", lastSeenName);
        }
        if (lastSeenId != null) {
            builder.queryParam("lastSeenId", lastSeenId);
        }

        String fullResourcePath = builder.build(false).toUriString();

        return get(fullResourcePath, headers);
    }
}
