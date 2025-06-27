package ru.pavbatol.myplace.gateway.profile.geo.region.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.profile.geo.region.RegionDto;

/**
 * REST client implementation for Region operations.
 * Handles HTTP communication with the Region service endpoints.
 */
@Component
public class RegionClientImpl extends BaseRestClient implements RegionClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/regions";
    private static final String PRIVATE_CONTEXT = "/user/geo/regions";

    /**
     * Constructs a new RegionClientImpl with server URL and RestTemplate.
     * @param serverUrl Base URL of the Region service
     * @param restTemplate Configured RestTemplate for HTTP operations
     */
    public RegionClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                            @Autowired RestTemplate restTemplate) {
        super(restTemplate, serverUrl);
    }

    /**
     * Sends create Region request to the service.
     * @param dto Region data to create
     * @param headers HTTP headers for the request
     * @return ResponseEntity with raw service response
     */
    @Override
    public ResponseEntity<Object> create(RegionDto dto, HttpHeaders headers) {
        return post(ADMIN_CONTEXT, headers, dto);
    }

    /**
     * Sends update Region request to the service.
     * @param regionId ID of the Region to update
     * @param dto Updated Region data
     * @param headers HTTP headers for the request
     * @return ResponseEntity with raw service response
     */
    @Override
    public ResponseEntity<Object> update(Long regionId, RegionDto dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s", regionId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    /**
     * Sends delete Region request to the service.
     * @param regionId ID of the Region to delete
     * @param headers HTTP headers for the request
     * @return ResponseEntity with raw service response
     */
    @Override
    public ResponseEntity<Object> delete(Long regionId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", regionId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    /**
     * Retrieves Region by ID from the service.
     * @param regionId ID of the Region to retrieve
     * @param headers HTTP headers for the request
     * @return ResponseEntity with raw service response
     */
    @Override
    public ResponseEntity<Object> getById(Long regionId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", regionId);
        String fullResourcePath = PRIVATE_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    /**
     * Retrieves paginated Regions from the service.
     * @param nameStartWith Filter by name prefix (optional)
     * @param lastSeenName Pagination cursor for region name (optional)
     * @param lastSeenCountryName Pagination cursor for country name (optional)
     * @param size Number of items per page
     * @param headers HTTP headers for the request
     * @return ResponseEntity with raw service response
     */
    @Override
    public ResponseEntity<Object> getAll(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size, HttpHeaders headers) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(PRIVATE_CONTEXT)
                .queryParam("size", size);

        if (nameStartWith != null) {
            builder.queryParam("nameStartWith", nameStartWith);
        }
        if (lastSeenName != null) {
            builder.queryParam("lastSeenName", lastSeenName);
        }
        if (lastSeenCountryName != null) {
            builder.queryParam("lastSeenCountryName", lastSeenCountryName);
        }

        String fullResourcePath = builder.build(false).toUriString();

        return get(fullResourcePath, headers);
    }
}
