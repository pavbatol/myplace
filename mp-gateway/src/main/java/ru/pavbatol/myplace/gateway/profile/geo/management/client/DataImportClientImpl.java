package ru.pavbatol.myplace.gateway.profile.geo.management.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataImportClientImpl implements DataImportClient {
    private static final String ADMIN_CONTEXT = "/admin/geo/upload";

    @Value("${app.mp.profile.url}")
    private String serverUrl;

    @Qualifier("longOperationTemplate")
    private final RestTemplate restTemplate;

    @Override
    public ResponseEntity<StreamingResponseBody> importDataFromCsv(MultipartFile file,
                                                                   boolean responseExportWithId,
                                                                   HttpHeaders headers) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String url = UriComponentsBuilder.fromUri(URI.create(serverUrl))
                .path(ADMIN_CONTEXT)
                .path("/csv")
                .queryParam("responseExportWithId", responseExportWithId)
                .build(false)
                .toUriString();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"geo-data-load-report.csv\"")
                .body(outputStream -> {
                    try {
                        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
                        parts.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

                        HttpHeaders filteredHeaders = new HttpHeaders();
                        headers.forEach((key, values) -> {
                            if (!key.equalsIgnoreCase("Content-Type") &&
                                    !key.equalsIgnoreCase("Content-Length")) {
                                filteredHeaders.addAll(key, values);
                            }
                        });

                        restTemplate.execute(
                                url,
                                HttpMethod.POST,
                                request -> {
                                    new FormHttpMessageConverter().write(parts, MediaType.MULTIPART_FORM_DATA, request);
                                },
                                response -> {
                                    InputStream bodyStream = response.getBody();
                                    byte[] buffer = new byte[8192];
                                    int bytesRead;
                                    long totalBytes = 0;
                                    log.debug("Trying reading from InputStream");
                                    while ((bytesRead = bodyStream.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, bytesRead);
                                        outputStream.flush();
                                        totalBytes += bytesRead;
                                        log.debug("Sent {} bytes (total: {})", bytesRead, totalBytes);
                                    }
                                    return null;
                                },
                                filteredHeaders
                        );
                    } catch (IOException e) {
                        log.error("Streaming error", e);
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Streaming failed", e);
                    }
                });
    }

    private static class MultipartInputStreamFileResource extends InputStreamResource {
        private final String filename;

        public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return filename;
        }

        @Override
        public long contentLength() {
            return -1;
        }
    }
}
