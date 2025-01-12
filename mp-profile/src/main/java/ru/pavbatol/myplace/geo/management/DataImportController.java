package ru.pavbatol.myplace.geo.management;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("admin/geo/upload")
@RequiredArgsConstructor
public class DataImportController {
    private final DataImportService dataImportService;

    @PostMapping("/csv")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        log.debug("POST uploadCsv with file sized {} byte", file.getSize());
        long maxFileSizeMb = 5;

        if (file.isEmpty()) {
            return "The file is empty!";
        }

        if (file.getSize() > maxFileSizeMb * 1024 * 1024) {
            return String.format("The file size exceeds the allowed limit of %d MB.", maxFileSizeMb);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.endsWith(".csv")) {
            return "Invalid file format. A file with the .csv extension was expected.";
        }

        String contentType = file.getContentType();
        if (!"text/csv".equals(contentType) && !"application/vnd.ms-excel".equals(contentType)) {
            return "Неверный тип файла. Ожидался текстовый CSV файл.";
        }

        dataImportService.importDataFromCsv(file);
        return "The file has been uploaded and processed successfully.";
    }
}

