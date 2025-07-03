package ru.pavbatol.myplace.gateway.profile.geo.district.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.shared.dto.profile.geo.district.DistrictDto;

public interface DistrictClient {
    ResponseEntity<Object> create(DistrictDto dto, HttpHeaders headers);

    ResponseEntity<Object> update(Long districtId, DistrictDto dto, HttpHeaders headers);

    ResponseEntity<Object> delete(Long districtId, HttpHeaders headers);

    ResponseEntity<Object> getById(Long districtId, HttpHeaders headers);

    ResponseEntity<Object> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers);
}
