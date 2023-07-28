package ru.pavbatol.myplace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.client.ViewStatsClient;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
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
        ViewStatsClient webClient = new ViewStatsClient("http://localhost:9090");

        List<ViewDtoResponse> viewsAsList;
        try {
            viewsAsList = webClient.findByBlocking(new ViewSearchFilter(
                    LocalDateTime.now().minusYears(5),
                    LocalDateTime.now().plusYears(5),
                    List.of("uriMono", "uri"),
                    false,
                    SortDirection.DESC
            ));
            viewsAsList.forEach(System.out::println);
        } catch (Exception e) {
            log.warn("! Failed to getViewsAsList from stats module:", e);
        }

        Flux<ViewDtoResponse> viewsFlax = webClient.find(new ViewSearchFilter(
                LocalDateTime.now().minusYears(5),
                LocalDateTime.now().plusYears(5),
                List.of("uri-test"),
                true,
                SortDirection.DESC
        ));

        viewsFlax.subscribe(
                System.out::println,
                error -> System.err.println("An error occurred: " + error.getMessage())
        );

        viewsFlax = webClient.find(new ViewSearchFilter(
                LocalDateTime.now().minusYears(5),
                LocalDateTime.now().plusYears(5),
                List.of("uri-block"),
                true,
                SortDirection.DESC
        ));

        viewsFlax.subscribe(
                System.out::println,
                error -> System.err.println("An error occurred: " + error.getMessage())
        );

        //-- add
//        ViewDtoAddResponse viewDtoAddResponse = webClient.addByBlocking(new ViewDtoAddRequest(
//                "app-block",
//                "uri-block",
//                "ip-block",
//                LocalDateTime.now().plusYears(1)
//        ));
//        System.out.println(viewDtoAddResponse);
//
//        webClient.add(new ViewDtoAddRequest(
//                "appMono",
//                "uriMono",
//                "ipMono",
//                LocalDateTime.now().plusYears(2)
//        )).subscribe(System.out::println, throwable -> System.out.println(throwable.toString()));

        log.debug("! End testing stats module");
    }
}
