package ru.pavbatol.myplace.gateway.profile.geo.country.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.shared.dto.profile.geo.country.CountryDto;

public interface CountryClient {
    ResponseEntity<Object> create(CountryDto dto, HttpHeaders headers);

    ResponseEntity<Object> update(Long countryId, CountryDto dto, HttpHeaders headers);

    ResponseEntity<Object> delete(Long countryId, HttpHeaders headers);

    ResponseEntity<Object> getById(Long countryId, HttpHeaders headers);

    ResponseEntity<Object> getAll(String nameStartWith, int page, int size, HttpHeaders headers);
}
