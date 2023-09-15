package ru.pavbatol.myplace.server.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddRequest;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;
import ru.pavbatol.myplace.server.shipping.repository.ShippingGeoMongoRepository;
import ru.pavbatol.myplace.server.shipping.service.ShippingGeoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShippingGeoServiceImplTest {
    private final ShippingGeoService service;
    private final ShippingGeoMongoRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void findShippingCountryCities_shouldReturnGropingById() {
        ShippingGeoDtoAddRequest dto = new ShippingGeoDtoAddRequest(
                1L,
                "RF",
                "MOS",
                LocalDateTime.now().minusMinutes(1)
        );

        ShippingGeoDtoAddRequest dto1 = new ShippingGeoDtoAddRequest(
                2L,
                "RF",
                "MOS",
                LocalDateTime.now().minusMinutes(1)
        );

        ShippingGeoDtoAddRequest dto2 = new ShippingGeoDtoAddRequest(
                2L,
                "RF",
                "MOS",
                LocalDateTime.now().minusMinutes(1)
        );

        service.add(dto).block();
        service.add(dto1).block();
        service.add(dto2).block();

        List<ShippingGeoDtoResponse> responses = service.findShippingCountryCities(new ShippingGeoSearchFilter())
                .collectList().block();

        assertThat(responses).hasSize(2);
    }
}
