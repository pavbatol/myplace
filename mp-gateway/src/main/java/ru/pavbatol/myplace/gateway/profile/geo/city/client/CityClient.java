package ru.pavbatol.myplace.gateway.profile.geo.city.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.shared.dto.profile.geo.city.CityDto;

public interface CityClient {
    ResponseEntity<Object> create(CityDto dto, HttpHeaders headers);

    ResponseEntity<Object> update(Long cityId, CityDto dto, HttpHeaders headers);

    ResponseEntity<Object> delete(Long cityId, HttpHeaders headers);

    ResponseEntity<Object> getById(Long cityId, HttpHeaders headers);

    ResponseEntity<Object> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers);
}
