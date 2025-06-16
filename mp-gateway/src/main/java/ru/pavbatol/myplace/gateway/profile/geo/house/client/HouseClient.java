package ru.pavbatol.myplace.gateway.profile.geo.house.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.shared.dto.profile.geo.house.HouseDto;

public interface HouseClient {
    ResponseEntity<Object> create(HouseDto dto, HttpHeaders headers);

    ResponseEntity<Object> update(Long houseId, HouseDto dto, HttpHeaders headers);

    ResponseEntity<Object> delete(Long houseId, HttpHeaders headers);

    ResponseEntity<Object> getById(Long houseId, HttpHeaders headers);

    ResponseEntity<Object> getAll(String numberStartWith, String lastSeenNumber, Long lastSeenId, int size, HttpHeaders headers);
}
