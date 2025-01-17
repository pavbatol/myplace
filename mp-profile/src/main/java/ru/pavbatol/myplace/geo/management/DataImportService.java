package ru.pavbatol.myplace.geo.management;

import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;

public interface DataImportService {
    void importDataFromCsv(OutputStream outputStream, MultipartFile file);
}
