package ru.pavbatol.myplace.server.view.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.server.view.service.ViewService;

import java.time.LocalDateTime;

@WebFluxTest(controllers = ViewController.class)
class ViewControllerTest {
    public static final String URL_TEMPLATE = "/stats/views";

    @MockBean
    private ViewService service;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void add_shouldReturnCreatedStatus_whenRequestDtoIsCorrect() {
        ViewDtoAddRequest requestDto = new ViewDtoAddRequest("app", "uri", "ip", LocalDateTime.now());

        webTestClient.post().uri(URL_TEMPLATE)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ViewDtoAddResponse.class);
    }

    @Test
    void find_shouldReturnOkStatus_when() {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.add("pageNumber", "1");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path(URL_TEMPLATE)
                        .queryParams(multiValueMap)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ViewDtoResponse.class);
    }
}