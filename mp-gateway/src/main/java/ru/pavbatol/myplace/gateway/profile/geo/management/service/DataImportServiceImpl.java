package ru.pavbatol.myplace.gateway.profile.geo.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.profile.geo.management.client.DataImportClient;

@Component
@RequiredArgsConstructor
public class DataImportServiceImpl implements DataImportService {
    private final DataImportClient client;
    private final ResponseHandler responseHandler;

    @Override
    public ResponseEntity<StreamingResponseBody> importDataFromCsv(MultipartFile file, boolean responseExportWithId, HttpHeaders headers) {
        return client.importDataFromCsv(file, responseExportWithId, headers);
    }
}
