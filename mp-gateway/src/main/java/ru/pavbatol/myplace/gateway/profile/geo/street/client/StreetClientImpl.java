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

/**
 * REST client implementation for Street operations.
 * Handles HTTP communication with the Street service endpoints.
 */
@Component
public class StreetClientImpl extends BaseRestClient implements StreetClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/streets";
    private static final String PRIVATE_CONTEXT = "/user/geo/streets";

    public StreetClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                            @Autowired RestTemplate restTemplate) {
        super(restTemplate, serverUrl);
    }

    /**
     * Sends create Street request to the service.
     *
     * @param dto     Street data to create
     * @param headers HTTP headers for the request
     * @return ResponseEntity with raw service response
     */
    @Override
    public ResponseEntity<Object> create(StreetDto dto, HttpHeaders headers) {
        return post(ADMIN_CONTEXT, headers, dto);
    }

    /**
     * Sends update Street request to the service.
     *
     * @param streetId ID of the Street to update
     * @param dto      Updated Street data
     * @param headers  HTTP headers for the request
     * @return ResponseEntity with raw service response
     */
    @Override
    public ResponseEntity<Object> update(Long streetId, StreetDto dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s", streetId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    /**
     * Sends delete Street request to the service.
     *
     * @param streetId ID of the Street to delete
     * @param headers  HTTP headers for the request
     * @return ResponseEntity with raw service response
     */
    @Override
    public ResponseEntity<Object> delete(Long streetId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", streetId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    /**
     * Retrieves Street by ID from the service.
     *
     * @param streetId ID of the Street to retrieve
     * @param headers  HTTP headers for the request
     * @return ResponseEntity with raw service response
     */
    @Override
    public ResponseEntity<Object> getById(Long streetId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", streetId);
        String fullResourcePath = PRIVATE_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    /**
     * Retrieves paginated Streets from the service.
     *
     * @param nameStartWith Filter by name prefix (optional)
     * @param lastSeenName  Pagination cursor for Street name (optional)
     * @param lastSeenId    Pagination cursor for Street id (optional)
     * @param size          Number of items per page
     * @param headers       HTTP headers for the request
     * @return ResponseEntity with raw service response
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
