package ru.pavbatol.myplace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.client.impl.CartItemStatsClient;
import ru.pavbatol.myplace.client.impl.ViewStatsClient;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.cart.*;
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
//        try {
//            viewsAsList = webClient.findByBlocking(ViewSearchFilter.builder()
//                    .start(LocalDateTime.now().minusYears(5))
//                    .end(LocalDateTime.now().plusYears(5))
//                    .uris(List.of("uriMono", "uri"))
//                    .unique(false)
//                    .sortDirection(SortDirection.DESC)
//                    .build()
//            );
//            viewsAsList.forEach(System.out::println);
//        } catch (Exception e) {
//            log.warn("! Failed to getViewsAsList from stats module:", e);
//        }
//
//        webClient.find(ViewSearchFilter.builder()
//                        .start(LocalDateTime.now().minusYears(5))
//                        .end(LocalDateTime.now().plusYears(5))
//                        .uris(List.of("uri-test"))
//                        .unique(true)
//                        .sortDirection(SortDirection.DESC)
//                        .build()
//                )
//                .subscribe(
//                        System.out::println,
//                        error -> System.err.println("An error occurred: " + error.getMessage())
//                );
//
//        webClient.find(ViewSearchFilter.builder()
//                        .start(LocalDateTime.now().minusYears(5))
//                        .end(LocalDateTime.now().plusYears(5))
//                        .uris(List.of("uri-block"))
//                        .unique(true)
//                        .sortDirection(SortDirection.DESC)
//                        .build()
//                )
//                .subscribe(
//                        System.out::println,
//                        error -> System.err.println("An error occurred: " + error.getMessage())
//                );

        webClient.find(ViewSearchFilter.builder()
                        .start(LocalDateTime.now().minusYears(5))
                        .end(LocalDateTime.now().plusYears(5))
                        .uris(null)
                        .unique(true)
                        .sortDirection(SortDirection.DESC)
                        .build()
                )
                .subscribe(
                        System.out::println,
                        error -> System.err.println("An error occurred: " + error.getMessage())
                );

        webClient.find(ViewSearchFilter.builder()
                        .start(LocalDateTime.now().minusYears(5))
                        .end(LocalDateTime.now().plusYears(5))
                        .uris(null)
                        .unique(false)
                        .sortDirection(SortDirection.DESC)
                        .build()
                )
                .subscribe(
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
//        try {
//            cartItemAsList = webClientCart.findByBlocking(CartItemSearchFilter.builder()
//                    .start(LocalDateTime.now().minusYears(5))
//                    .end(LocalDateTime.now().plusYears(5))
//                    .itemIds(List.of(1L, 5L))
//                    .unique(false)
//                    .sortDirection(SortDirection.DESC)
//                    .build());
//            cartItemAsList.forEach(System.out::println);
//        } catch (Exception e) {
//            log.warn("! Failed to findByBlocking cartItems from stats module:", e);
//        }
//
//        webClientCart.find(CartItemSearchFilter.builder()
//                        .start(LocalDateTime.now().minusYears(5))
//                        .end(LocalDateTime.now().plusYears(5))
//                        .itemIds(List.of(1L, 5L))
//                        .unique(true)
//                        .sortDirection(SortDirection.DESC)
//                        .build()
//                )
//                .subscribe(
//                        System.out::println,
//                        error -> System.err.println("An error occurred: " + error.getMessage()));
//
//        webClientCart.find(CartItemSearchFilter.builder()
//                        .start(LocalDateTime.now().minusYears(5))
//                        .end(LocalDateTime.now().plusYears(5))
//                        .itemIds(null)
//                        .unique(false)
//                        .sortDirection(SortDirection.DESC)
//                        .build())
//                .subscribe(
//                        System.out::println,
//                        error -> System.err.println("An error occurred: " + error.getMessage()));
//
//        webClientCart.find(CartItemSearchFilter.builder()
//                        .start(LocalDateTime.now().minusYears(5))
//                        .end(LocalDateTime.now().plusYears(5))
//                        .itemIds(null)
//                        .unique(true)
//                        .sortDirection(SortDirection.DESC)
//                        .build())
//                .subscribe(
//                        System.out::println,
//                        error -> System.err.println("An error occurred: " + error.getMessage()));

        //--
//        webClientCart.findUserCartItems(UserCartItemSearchFilter.builder()
//                        .start(LocalDateTime.now().minusYears(5))
//                        .end(LocalDateTime.now().plusYears(5))
//                        .userIds(List.of(10L, 2L))
//                        .unique(false)
//                        .sortDirection(SortDirection.DESC)
//                        .build())
//                .subscribe(
//                        System.out::println,
//                        error -> System.err.println("An error occurred: " + error.getMessage()));

//        webClientCart.findUserCartItems(UserCartItemSearchFilter.builder()
//                        .start(LocalDateTime.now().minusYears(5))
//                        .end(LocalDateTime.now().plusYears(5))
//                        .userIds(List.of(10L, 2L))
//                        .unique(true)
//                        .sortDirection(SortDirection.DESC)
//                        .build())
//                .subscribe(
//                        System.out::println,
//                        error -> System.err.println("An error occurred: " + error.getMessage()));

//        webClientCart.findUserCartItems(UserCartItemSearchFilter.builder()
//                        .start(LocalDateTime.now().minusYears(5))
//                        .end(LocalDateTime.now().plusYears(5))
//                        .userIds(null)
//                        .unique(false)
//                        .sortDirection(SortDirection.DESC)
//                        .build())
//                .subscribe(
//                        System.out::println,
//                        error -> System.err.println("An error occurred: " + error.getMessage()));
//
//        webClientCart.findUserCartItems(UserCartItemSearchFilter.builder()
//                        .start(LocalDateTime.now().minusYears(5))
//                        .end(LocalDateTime.now().plusYears(5))
//                        .userIds(null)
//                        .unique(true)
//                        .sortDirection(SortDirection.DESC)
//                        .build())
//                .subscribe(
//                        System.out::println,
//                        error -> System.err.println("An error occurred: " + error.getMessage()));

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
