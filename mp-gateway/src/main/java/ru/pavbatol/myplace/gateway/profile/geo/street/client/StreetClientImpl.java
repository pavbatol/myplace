package ru.pavbatol.myplace.gateway.profile.geo.street.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.profile.geo.street.StreetDto;

@Component
public class StreetClientImpl extends BaseRestClient implements StreetClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/streets";
    private static final String PRIVATE_CONTEXT = "/user/geo/streets";

    public StreetClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                            @Autowired RestTemplate restTemplate) {
        super(restTemplate, serverUrl);
    }

    @Override
    public ResponseEntity<Object> create(StreetDto dto, HttpHeaders headers) {
        return post(ADMIN_CONTEXT, headers, dto);
    }

    @Override
    public ResponseEntity<Object> update(Long streetId, StreetDto dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s", streetId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    @Override
    public ResponseEntity<Object> delete(Long streetId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", streetId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> getById(Long streetId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", streetId);
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
