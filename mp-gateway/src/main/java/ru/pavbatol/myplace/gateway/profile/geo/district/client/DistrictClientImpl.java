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

/**
 * REST client implementation for District operations.
 * Handles HTTP communication with District service endpoints.
 */
@Component
public class DistrictClientImpl extends BaseRestClient implements DistrictClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/districts";
    private static final String PRIVATE_CONTEXT = "/user/geo/districts";

    /**
     * Constructs DistrictClientImpl with service URL and RestTemplate.
     * @param serverUrl Base service URL
     * @param restTemplate Configured RestTemplate for HTTP requests
     */
    public DistrictClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                              @Autowired RestTemplate restTemplate) {
        super(restTemplate, serverUrl);
    }

    /**
     * Sends create District request to service.
     * @param dto District data to create
     * @param headers HTTP headers
     * @return Raw service response
     */
    @Override
    public ResponseEntity<Object> create(DistrictDto dto, HttpHeaders headers) {
        return post(ADMIN_CONTEXT, headers, dto);
    }

    /**
     * Sends update District request to service.
     * @param districtId District ID to update
     * @param dto Updated District data
     * @param headers HTTP headers
     * @return Raw service response
     */
    @Override
    public ResponseEntity<Object> update(Long districtId, DistrictDto dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s", districtId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    /**
     * Sends delete District request to service.
     * @param districtId District ID to delete
     * @param headers HTTP headers
     * @return Raw service response
     */
    @Override
    public ResponseEntity<Object> delete(Long districtId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", districtId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    /**
     * Retrieves District by ID from service.
     * @param districtId District ID to retrieve
     * @param headers HTTP headers
     * @return Raw service response
     */
    @Override
    public ResponseEntity<Object> getById(Long districtId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", districtId);
        String fullResourcePath = PRIVATE_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    /**
     * Retrieves filtered/paginated Districts from service.
     * @param nameStartWith Name filter prefix
     * @param lastSeenName Pagination name cursor
     * @param lastSeenId Pagination ID cursor
     * @param size Page size (1-100)
     * @param headers HTTP headers
     * @return Raw service response
     */
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
