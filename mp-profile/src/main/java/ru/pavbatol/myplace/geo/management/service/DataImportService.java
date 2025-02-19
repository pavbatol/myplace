package ru.pavbatol.myplace.geo.management.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

public interface DataImportService {
    void importDataFromCsv(OutputStream outputStream, MultipartFile file, boolean responseExportWithId);
}
