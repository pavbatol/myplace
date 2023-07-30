package ru.pavbatol.myplace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.client.impl.CartItemStatsClient;
import ru.pavbatol.myplace.client.impl.ViewStatsClient;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddRequest;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddResponse;
import ru.pavbatol.myplace.dto.cart.CartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.CartItemSearchFilter;
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

        //-- ViewStatsClient
        //-- get
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


        //-- CartItemStatsClient
        //-- get
        CartItemStatsClient webClientCart = new CartItemStatsClient("http://localhost:9090");

        List<CartItemDtoResponse> cartItemAsList;
        try {
            cartItemAsList = webClientCart.findByBlocking(new CartItemSearchFilter(
                    LocalDateTime.now().minusYears(5),
                    LocalDateTime.now().plusYears(5),
                    List.of(1L, 5L),
                    false,
                    SortDirection.DESC
            ));
            cartItemAsList.forEach(System.out::println);
        } catch (Exception e) {
            log.warn("! Failed to findByBlocking cartItems from stats module:", e);
        }

        webClientCart.find(new CartItemSearchFilter(
                LocalDateTime.now().minusYears(5),
                LocalDateTime.now().plusYears(5),
                List.of(1L, 5L),
                true,
                SortDirection.DESC
        )).subscribe(
                System.out::println,
                error -> System.err.println("An error occurred: " + error.getMessage()));

        webClientCart.find(new CartItemSearchFilter(
                LocalDateTime.now().minusYears(5),
                LocalDateTime.now().plusYears(5),
                null,
                false,
                SortDirection.DESC
        )).subscribe(
                System.out::println,
                error -> System.err.println("An error occurred: " + error.getMessage()));

        //-- add
//        CartItemDtoAddResponse cartItemDtoAddResponse = webClientCart.addByBlocking(new CartItemDtoAddRequest(
//                10L,
//                15L,
//                LocalDateTime.now().plusYears(1)
//        ));
//        System.out.println(cartItemDtoAddResponse);
//
//        webClientCart.add(new CartItemDtoAddRequest(
//                10L,
//                15L,
//                LocalDateTime.now().plusYears(1)
//        )).subscribe(System.out::println, throwable -> System.out.println(throwable.toString()));


        log.debug("! End testing stats module");
    }
}
