package ru.pavbatol.myplace.gateway.profile.geo.region.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.shared.dto.profile.geo.region.RegionDto;

public interface RegionClient {
    ResponseEntity<Object> create(RegionDto dto, HttpHeaders headers);

    ResponseEntity<Object> update(Long regionId, RegionDto dto, HttpHeaders headers);

    ResponseEntity<Object> delete(Long regionId, HttpHeaders headers);

    ResponseEntity<Object> getById(Long regionId, HttpHeaders headers);

    ResponseEntity<Object> getAll(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size, HttpHeaders headers);
}
