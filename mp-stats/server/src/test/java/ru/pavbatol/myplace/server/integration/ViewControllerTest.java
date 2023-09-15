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
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.server.view.model.View;
import ru.pavbatol.myplace.server.view.repository.ViewMongoRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ViewControllerTest {
    public static final String URL_TEMPLATE = "/stats/views";
    private final WebTestClient webTestClient;
    private final ViewMongoRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    public void add_shouldStatusIsCreatedAndResponseObjectFieldsAreEqualToFieldsOfRequest() {
        LocalDateTime dateTime = LocalDateTime.now();
        ViewDtoAddRequest requestDto = new ViewDtoAddRequest("app", "uri", "ip", dateTime);

        webTestClient.post().uri(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestDto), ViewDtoAddRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ViewDtoAddResponse.class)
                .value(viewDtoAddResponse -> {
                            assertThat(viewDtoAddResponse).isNotNull();
                            assertThat(viewDtoAddResponse.getId()).isNotNull();
                            assertThat(viewDtoAddResponse.getIp()).isEqualTo(requestDto.getIp());
                            assertThat(viewDtoAddResponse.getApp()).isEqualTo(requestDto.getApp());
                            assertThat(viewDtoAddResponse.getUri()).isEqualTo(requestDto.getUri());
                        }
                );
    }

    @Test
    void find_shouldCorrectCountingViews() {
        View view = new View()
                .setApp("app")
                .setUri("uri")
                .setIp("ip")
                .setTimestamp(LocalDateTime.now().minusMinutes(1));

        View view0 = new View()
                .setApp("app")
                .setUri("uri")
                .setIp("ip0")
                .setTimestamp(LocalDateTime.now().minusMinutes(1));

        View view1 = new View()
                .setApp("app1")
                .setUri("uri1")
                .setIp("ip1")
                .setTimestamp(LocalDateTime.now().minusMinutes(1));

        repository.saveAll(List.of(view, view0, view1)).collectList().block();

        webTestClient.get().uri(URL_TEMPLATE)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBodyList(ViewDtoResponse.class)
                .hasSize(2)
                .value(dtoList -> {
                    assertThat(dtoList.get(0).getViews()).isEqualTo(2);
                    assertThat(dtoList.get(1).getViews()).isEqualTo(1);
                    dtoList.forEach(System.out::println);
                });
    }
}