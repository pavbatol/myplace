package ru.pavbatol.myplace.gateway.profile.geo.city.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.profile.geo.city.CityDto;

/**
 * REST client implementation for City operations.
 * Handles HTTP communication with City service endpoints.
 */
@Component
public class CityClientImpl extends BaseRestClient implements CityClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/cities";
    private static final String PRIVATE_CONTEXT = "/user/geo/cities";

    public CityClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                          @Autowired RestTemplate restTemplate) {
        super(restTemplate, serverUrl);
    }

    /**
     * Sends create City request to service.
     *
     * @param dto     City data to create
     * @param headers HTTP headers for the request
     * @return Raw service response
     */
    @Override
    public ResponseEntity<Object> create(CityDto dto, HttpHeaders headers) {
        return post(ADMIN_CONTEXT, headers, dto);
    }

    /**
     * Sends update City request to service.
     *
     * @param cityId  ID of City to update
     * @param dto     Updated City data
     * @param headers HTTP headers for the request
     * @return Raw service response
     */
    @Override
    public ResponseEntity<Object> update(Long cityId, CityDto dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s", cityId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    /**
     * Sends delete City request to service.
     *
     * @param cityId  ID of City to delete
     * @param headers HTTP headers for the request
     * @return Raw service response
     */
    @Override
    public ResponseEntity<Object> delete(Long cityId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", cityId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    /**
     * Retrieves City by ID from service.
     *
     * @param cityId  ID of City to retrieve
     * @param headers HTTP headers for the request
     * @return Raw service response
     */
    @Override
    public ResponseEntity<Object> getById(Long cityId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", cityId);
        String fullResourcePath = PRIVATE_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    /**
     * Retrieves filtered/paginated Cities from service.
     *
     * @param nameStartWith Filter by name prefix (optional)
     * @param lastSeenName  Pagination cursor by name (optional)
     * @param lastSeenId    Pagination cursor by ID (optional)
     * @param size          Number of items per page
     * @param headers       HTTP headers for the request
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
