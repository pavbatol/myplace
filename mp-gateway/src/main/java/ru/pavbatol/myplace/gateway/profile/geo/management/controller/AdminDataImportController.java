package ru.pavbatol.myplace.gateway.profile.geo.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.pavbatol.myplace.gateway.app.access.RequiredRoles;
import ru.pavbatol.myplace.gateway.profile.geo.management.client.DataImportClient;
import ru.pavbatol.myplace.gateway.profile.geo.management.service.DataImportService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/${app.mp.profile.label}/admin/geo/upload")
@RequiredArgsConstructor
@Tag(name = "[Profile/Geo]Management/Import: Admin", description = "API for importing geo data from CSV")
public class AdminDataImportController {

    private static final String ADMIN = "ADMIN";
    private final DataImportService dataImportService;

    private final DataImportClient client;

    @Value("${spring.application.name:unknownAppName}")
    private String serviceName;

    @RequiredRoles(roles = {ADMIN})
    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "import data", description = "importing geo data from CSV file")
    public ResponseEntity<StreamingResponseBody> uploadCsv(
            @Parameter(description = "CSV file to upload", required = true) @RequestPart("file") MultipartFile file,
            @RequestParam(value = "responseExportWithId", defaultValue = "false") boolean responseExportWithId,
            @RequestHeader HttpHeaders headers) {
        log.debug("POST uploadCsv with file sized {} byte; responseExportWithId={}", file.getSize(), responseExportWithId);

        validateFile(file);
        return dataImportService.importDataFromCsv(file, responseExportWithId, headers);
    }

    private void validateFile(MultipartFile file) {
        long maxFileSizeMb = 5;

        if (file.isEmpty()) {
            throw new IllegalArgumentException("he file is empty!");
        }

        if (file.getSize() > maxFileSizeMb * 1024 * 1024) {
            throw new IllegalArgumentException(String.format("The file size exceeds the allowed limit of %d MB.", maxFileSizeMb));
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Invalid file format. A file with the .csv extension was expected.");
        }

        String contentType = file.getContentType();
        if (!"text/csv".equals(contentType) && !"application/vnd.ms-excel".equals(contentType)) {
            String errMessage = "Invalid file type. A text CSV file was expected. But file.getContentType()=" + contentType;
            log.debug(errMessage);
            throw new IllegalArgumentException(errMessage);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String firstLine = reader.readLine();
            if (firstLine == null || !firstLine.contains(",")) {
                throw new IllegalArgumentException("Invalid CSV: no comma delimiter found");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read file", e);
        }
    }
}
