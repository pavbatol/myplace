package ru.pavbatol.myplace.gateway.profile.geo.street.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import ru.pavbatol.myplace.shared.dto.profile.geo.street.StreetDto;

public interface StreetClient {
    ResponseEntity<Object> create(StreetDto dto, HttpHeaders headers);

    ResponseEntity<Object> update(Long streetId, StreetDto dto, HttpHeaders headers);

    ResponseEntity<Object> delete(Long streetId, HttpHeaders headers);

    ResponseEntity<Object> getById(Long streetId, HttpHeaders headers);

    ResponseEntity<Object> getAll(String nameStartWith, String lastSeenName, Long lastSeenId, int size, HttpHeaders headers);
}
