package ru.pavbatol.myplace.geo.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.pavbatol.myplace.geo.management.service.DataImportService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/geo/upload")
@RequiredArgsConstructor
@Tag(name = "Admin: Import geo data", description = "API for importing geo data from CSV")
public class AdminDataImportController {
    private final DataImportService dataImportService;

    @Value("${spring.application.name}")
    private String serviceName;

    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "import data", description = "importing geo data from CSV file")
    public ResponseEntity<StreamingResponseBody> uploadCsv(
            @Parameter(description = "CSV file to upload", required = true) @RequestPart("file") MultipartFile file,
            @RequestParam(value = "responseExportWithId", defaultValue = "false") boolean responseExportWithId) {
        log.debug("POST uploadCsv with file sized {} byte; responseExportWithId={}", file.getSize(), responseExportWithId);

        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        String fileName = String.join("_", serviceName, "geo-data-load-report", formattedDateTime) + ".csv";

        return ResponseEntity.ok()
                .headers(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/csv");
                })
                .body(outputStream -> dataImportService.importDataFromCsv(outputStream, file, responseExportWithId));
    }
}
