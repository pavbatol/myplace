package ru.pavbatol.myplace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.pavbatol.myplace.app.annotation.ExcludeJacocoGenerated;

// TODO: 13.09.2023 Remove 'ExcludeJacocoGenerated' annotation when developing this service
@Slf4j
@SpringBootApplication
@ExcludeJacocoGenerated
public class MainService {
    public static void main(String[] args) {
        SpringApplication.run(MainService.class, args);
    }
}
