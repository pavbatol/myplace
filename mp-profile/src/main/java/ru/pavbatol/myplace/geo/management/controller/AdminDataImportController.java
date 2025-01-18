package ru.pavbatol.myplace.geo.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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

    @PostMapping("/csv")
    @Operation(summary = "import data", description = "importing geo data from CSV file")
    public ResponseEntity<StreamingResponseBody> uploadCsv(
            @Parameter(description = "CSV file to upload", required = true) @RequestPart("file") MultipartFile file,
            @RequestParam(value = "responseExportWithId", defaultValue = "false") boolean responseExportWithId) {
        log.debug("POST uploadCsv with file sized {} byte; responseExportWithId={}", file.getSize(), responseExportWithId);
        long maxFileSizeMb = 5;

        if (file.isEmpty()) {
            throw new IllegalArgumentException("he file is empty!");
        }

        if (file.getSize() > maxFileSizeMb * 1024 * 1024) {
            throw new IllegalArgumentException(String.format("The file size exceeds the allowed limit of %d MB.", maxFileSizeMb));
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.endsWith(".csv")) {
            throw new IllegalArgumentException("Invalid file format. A file with the .csv extension was expected.");
        }

        String contentType = file.getContentType();
        if (!"text/csv".equals(contentType) && !"application/vnd.ms-excel".equals(contentType)) {
            throw new IllegalArgumentException("Invalid file type. A text CSV file was expected..");
        }

        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        String fileName = String.join("_", serviceName, "geo-data-load-report", formattedDateTime) + ".csv";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream -> dataImportService.importDataFromCsv(outputStream, file, responseExportWithId));
    }
}

