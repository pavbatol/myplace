package ru.pavbatol.myplace.server.cart.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddRequest;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddResponse;
import ru.pavbatol.myplace.dto.cart.CartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.UserCartItemDtoResponse;
import ru.pavbatol.myplace.server.cart.service.CartItemService;

import java.time.LocalDateTime;

@WebFluxTest(controllers = CartItemController.class)
class CartItemControllerTest {
    public static final String URL_TEMPLATE = "/stats";

    @MockBean
    private CartItemService service;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void add_shouldReturnCreatedStatus_whenRequestDtoIsCorrect() {
        CartItemDtoAddRequest requestDto = new CartItemDtoAddRequest(1L, 1L, LocalDateTime.now());
        String pathSegment = "/cartitems";

        webTestClient.post().uri(URL_TEMPLATE + pathSegment)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CartItemDtoAddResponse.class);
    }

    @Test
    void find_shouldReturnOkStatus_whenQueryParametersIsEmpty() {
        String pathSegment = "/cartitems";

        webTestClient.get().uri(uriBuilder -> uriBuilder.path(URL_TEMPLATE + pathSegment)
                        .query("")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CartItemDtoResponse.class);
    }

    @Test
    void findUserCartItems_shouldReturnOkStatus_whenQueryParametersIsEmpty() {
        String pathSegment = "/user/cartitems";

        webTestClient.get().uri(uriBuilder -> uriBuilder.path(URL_TEMPLATE + pathSegment)
                        .query("")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserCartItemDtoResponse.class);
    }
}