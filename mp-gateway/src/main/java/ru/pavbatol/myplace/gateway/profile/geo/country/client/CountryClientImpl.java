package ru.pavbatol.myplace.gateway.profile.geo.country.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.pavbatol.myplace.shared.client.BaseRestClient;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;

import java.util.HashMap;
import java.util.Map;

/**
 * REST client implementation for Country operations.
 * Handles communication with the Country microservice endpoints.
 */
@Component
public class CountryClientImpl extends BaseRestClient implements CountryClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/countries";
    private static final String PRIVATE_CONTEXT = "/user/geo/countries";

    public CountryClientImpl(@Value("${app.mp.profile.url}") String serverUrl,
                             @Autowired RestTemplate restTemplate) {
        super(restTemplate, serverUrl);
    }

    /**
     * Creates a new Country via REST API.
     * @param dto Country data to create
     * @param headers HTTP headers for the request
     * @return ResponseEntity with the API response
     */
    @Override
    public ResponseEntity<Object> create(CountryDto dto, HttpHeaders headers) {
        return post(ADMIN_CONTEXT, headers, dto);
    }

    /**
     * Updates an existing Country via REST API.
     * @param countryId ID of the Country to update
     * @param dto Updated Country data
     * @param headers HTTP headers for the request
     * @return ResponseEntity with the API response
     */
    @Override
    public ResponseEntity<Object> update(Long countryId, CountryDto dto, HttpHeaders headers) {
        String resourcePath = String.format("/%s", countryId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return patch(fullResourcePath, headers, dto);
    }

    /**
     * Deletes a Country via REST API.
     * @param countryId ID of the Country to delete
     * @param headers HTTP headers for the request
     * @return ResponseEntity with the API response
     */
    @Override
    public ResponseEntity<Object> delete(Long countryId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", countryId);
        String fullResourcePath = ADMIN_CONTEXT + resourcePath;

        return delete(fullResourcePath, headers);
    }

    /**
     * Retrieves a Country by ID via REST API.
     * @param countryId ID of the Country to retrieve
     * @param headers HTTP headers for the request
     * @return ResponseEntity with the API response
     */
    @Override
    public ResponseEntity<Object> getById(Long countryId, HttpHeaders headers) {
        String resourcePath = String.format("/%s", countryId);
        String fullResourcePath = PRIVATE_CONTEXT + resourcePath;

        return get(fullResourcePath, headers);
    }

    /**
     * Retrieves paginated list of Countries via REST API.
     * @param nameStartWith Filter by name prefix (optional)
     * @param lastSeenName Pagination cursor (optional)
     * @param size Number of items per page
     * @param headers HTTP headers for the request
     * @return ResponseEntity with the API response
     */
    @Override
    public ResponseEntity<Object> getAll(String nameStartWith, String lastSeenName, int size, HttpHeaders headers) {
        String paramsPath = "?" +
                "nameStartWith={nameStartWith}" +
                "&lastSeenName={lastSeenName}" +
                "&size={size}";
        String fullResourcePath = PRIVATE_CONTEXT + paramsPath;

        Map<String, Object> params = new HashMap<>(4);
        params.put("nameStartWith", nameStartWith);
        params.put("lastSeenName", lastSeenName);
        params.put("size", size);

        return get(fullResourcePath, headers, params);
    }
}
