package ru.pavbatol.myplace.geo.management;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("admin/geo/upload") // TODO: Add prefix for APi
@RequiredArgsConstructor
public class DataImportController {
    private final DataImportService dataImportService;

    @Value("${spring.application.name}")
    private String serviceName;

    @PostMapping("/csv")
    public ResponseEntity<StreamingResponseBody> uploadCsv(@RequestParam("file") MultipartFile file,
                                                           @RequestParam(value = "responseExportWithId", defaultValue = "false") boolean responseExportWithId) { // TODO: Add this into html form
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

