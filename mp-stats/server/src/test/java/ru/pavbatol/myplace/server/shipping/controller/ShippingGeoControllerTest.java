package ru.pavbatol.myplace.server.shipping.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddRequest;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.server.shipping.service.ShippingGeoService;

import java.time.LocalDateTime;

@WebFluxTest(controllers = ShippingGeoController.class)
class ShippingGeoControllerTest {
    public static final String URL_TEMPLATE = "/stats/shippinggeos";

    @MockBean
    private ShippingGeoService service;
    @Autowired
    private WebTestClient webTestClient;


    @Test
    void add_shouldReturnCreatedStatus_whenRequestDtoIsCorrect() {
        ShippingGeoDtoAddRequest requestDto = new ShippingGeoDtoAddRequest(1L, "RF", "Saratov",
                LocalDateTime.now());

        webTestClient.post().uri(URL_TEMPLATE)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ShippingGeoDtoAddResponse.class);

    }

    @Test
    void findShippingCountryCities_shouldReturnOkStatus_whenQueryParametersIsEmpty() {
        webTestClient.get().uri(uriBuilder -> uriBuilder.path(URL_TEMPLATE)
                        .query("")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ShippingGeoDtoResponse.class);
    }
}