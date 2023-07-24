package ru.pavbatol.myplace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.pavbatol.myplace.client.StatsClient;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootApplication
public class MainService {
    public static void main(String[] args) {
        SpringApplication.run(MainService.class, args);

        /**
         * Temporary code for testing the stats module
         */
        log.debug("! Start testing stats module");
        StatsClient webClient = new StatsClient("http://localhost:9090");

        List<ViewDtoResponse> viewsAsList = null;
        try {
            viewsAsList = webClient.getViewsAsList(new ViewSearchFilter(
                    LocalDateTime.now().minusYears(1),
                    LocalDateTime.now(),
                    List.of("/test_1/0", "/test_2/5"),
                    true
            ));
            viewsAsList.forEach(System.out::println);
            log.debug("! End testing stats module");
        } catch (Exception e) {
            log.warn("! Failed to getViewsAsList from stats module:", e);
        }
    }
}
