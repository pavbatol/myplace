package ru.pavbatol.myplace.geo.management;

import org.springframework.web.multipart.MultipartFile;

public interface DataImportService {
    void importDataFromCsv(MultipartFile file);
}
