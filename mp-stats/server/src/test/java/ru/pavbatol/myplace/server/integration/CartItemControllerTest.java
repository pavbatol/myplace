package ru.pavbatol.myplace.server.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddRequest;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddResponse;
import ru.pavbatol.myplace.dto.cart.CartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.UserCartItemDtoResponse;
import ru.pavbatol.myplace.server.cart.model.CartItem;
import ru.pavbatol.myplace.server.cart.repository.CartItemMongoRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CartItemControllerTest {
    public static final String URL_TEMPLATE = "/stats";
    private final WebTestClient webTestClient;
    private final CartItemMongoRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void add_shouldStatusIsCreatedAndResponseObjectFieldsAreEqualToFieldsOfRequest() {
        LocalDateTime dateTime = LocalDateTime.now();
        CartItemDtoAddRequest requestDto = new CartItemDtoAddRequest(2L, 5L, dateTime);

        String pathSegment = "/cartitems";
        webTestClient.post().uri(URL_TEMPLATE + pathSegment)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestDto), CartItemDtoAddRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CartItemDtoAddResponse.class)
                .value(cartItemDtoAddResponse -> {
                            assertThat(cartItemDtoAddResponse).isNotNull();
                            assertThat(cartItemDtoAddResponse.getUserId()).isEqualTo(requestDto.getUserId());
                            assertThat(cartItemDtoAddResponse.getItemId()).isEqualTo(requestDto.getItemId());
                            assertThat(cartItemDtoAddResponse.getTimestamp().truncatedTo(ChronoUnit.SECONDS))
                                    .isEqualTo(requestDto.getTimestamp().truncatedTo(ChronoUnit.SECONDS));
                        }
                );
    }

    @Test
    void find_shouldCorrectCountingCartItemCount() {
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);
        CartItem cartItem = new CartItem()
                .setUserId(1L)
                .setItemId(1L)
                .setTimestamp(dateTime);

        CartItem cartItem0 = new CartItem()
                .setUserId(1L)
                .setItemId(2L)
                .setTimestamp(dateTime);

        CartItem cartItem1 = new CartItem()
                .setUserId(2L)
                .setItemId(2L)
                .setTimestamp(dateTime);

        repository.saveAll(List.of(cartItem, cartItem0, cartItem1)).collectList().block();

        String pathSegment = "/cartitems";
        webTestClient.get().uri(URL_TEMPLATE + pathSegment)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBodyList(CartItemDtoResponse.class)
                .hasSize(2)
                .value(dtos -> {
                    assertThat(dtos.get(0).getCartItemCount()).isEqualTo(2);
                    assertThat(dtos.get(1).getCartItemCount()).isEqualTo(1);
                });
    }

    @Test
    void findUserCartItems_shouldCorrectCountingCartItemIds() {
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);
        CartItem cartItem = new CartItem()
                .setUserId(1L)
                .setItemId(1L)
                .setTimestamp(dateTime);

        CartItem cartItem0 = new CartItem()
                .setUserId(1L)
                .setItemId(2L)
                .setTimestamp(dateTime);

        CartItem cartItem1 = new CartItem()
                .setUserId(2L)
                .setItemId(2L)
                .setTimestamp(dateTime);

        repository.saveAll(List.of(cartItem, cartItem0, cartItem1)).collectList().block();

        String pathSegment = "/user/cartitems";
        webTestClient.get().uri(URL_TEMPLATE + pathSegment)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBodyList(UserCartItemDtoResponse.class)
                .hasSize(2)
                .value(dtos -> {
                    assertThat(dtos.get(0).getItemCount()).isEqualTo(2);
                    assertThat(dtos.get(0).getCartItemIds()).hasSize(2);
                    assertThat(dtos.get(1).getItemCount()).isEqualTo(1);
                    assertThat(dtos.get(1).getCartItemIds()).hasSize(1);
                });
    }
}