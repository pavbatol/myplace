package ru.pavbatol.myplace.gateway.profile.geo.management.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface DataImportService {
    ResponseEntity<StreamingResponseBody> importDataFromCsv(MultipartFile file, boolean responseExportWithId, HttpHeaders headers);
}
