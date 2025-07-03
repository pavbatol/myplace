package ru.pavbatol.myplace.gateway.profile.geo.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.pavbatol.myplace.gateway.app.api.ResponseHandler;
import ru.pavbatol.myplace.gateway.profile.geo.management.client.DataImportClient;

/**
 * Service implementation for importing geo data from CSV files.
 * Acts as an intermediary between controllers and the data import client.
 */
@Component
@RequiredArgsConstructor
public class DataImportServiceImpl implements DataImportService {
    private final DataImportClient client;
    private final ResponseHandler responseHandler;

    /**
     * Imports geo data from the provided CSV file.
     *
     * @param file CSV file containing data to import
     * @param responseExportWithId whether to include generated IDs in the response
     * @param headers HTTP headers for the request
     * @return ResponseEntity containing the import results as a streaming response
     */
    @Override
    public ResponseEntity<StreamingResponseBody> importDataFromCsv(MultipartFile file, boolean responseExportWithId, HttpHeaders headers) {
        return client.importDataFromCsv(file, responseExportWithId, headers);
    }
}
