package ru.pavbatol.myplace.gateway.profile.geo.district.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.profile.geo.district.DistrictDto;

@Component
public class DistrictClientImpl extends BaseRestClient implements DistrictClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/districts";
    private static final String PRIVATE_CONTEXT = "/user/geo/districts";

    public DistrictClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                              @Autowired RestTemplate restTemplate) {
        super(restTemplate, serverUrl);
    }

    @Override
    public ResponseEntity<Object> create(DistrictDto dto, HttpHeaders headers) {
        return post(ADMIN_CONTEXT, headers, dto);
    }

    @Override
    public ResponseEntity<Object> update(Long districtId, DistrictDto dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s", districtId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    @Override
    public ResponseEntity<Object> delete(Long districtId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", districtId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> getById(Long districtId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", districtId);
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
