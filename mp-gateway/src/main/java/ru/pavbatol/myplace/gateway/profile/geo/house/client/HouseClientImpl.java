package ru.pavbatol.myplace.gateway.profile.geo.house.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;

@Component
public class HouseClientImpl extends BaseRestClient implements HouseClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/houses";
    private static final String PRIVATE_CONTEXT = "/user/geo/houses";

    public HouseClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                           @Autowired RestTemplate restTemplate) {
        super(restTemplate, serverUrl);
    }

    @Override
    public ResponseEntity<Object> create(HouseDto dto, HttpHeaders headers) {
        return post(ADMIN_CONTEXT, headers, dto);
    }

    @Override
    public ResponseEntity<Object> update(Long houseId, HouseDto dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s", houseId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    @Override
    public ResponseEntity<Object> delete(Long houseId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", houseId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> getById(Long houseId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", houseId);
        String fullResourcePath = PRIVATE_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    @Override
    public ResponseEntity<Object> getAll(String numberStartWith, String lastSeenNumber, Long lastSeenId, int size, HttpHeaders headers) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(PRIVATE_CONTEXT)
                .queryParam("size", size);

        if (numberStartWith != null) {
            builder.queryParam("numberStartWith", numberStartWith);
        }
        if (lastSeenNumber != null) {
            builder.queryParam("lastSeenNumber", lastSeenNumber);
        }
        if (lastSeenId != null) {
            builder.queryParam("lastSeenId", lastSeenId);
        }

        String fullResourcePath = builder.build(false).toUriString();

        return get(fullResourcePath, headers);
    }
}
