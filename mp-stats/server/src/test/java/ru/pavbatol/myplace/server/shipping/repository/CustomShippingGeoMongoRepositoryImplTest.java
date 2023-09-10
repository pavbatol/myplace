package ru.pavbatol.myplace.server.shipping.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;
import ru.pavbatol.myplace.server.app.context.ApplicationContextProvider;
import ru.pavbatol.myplace.server.shipping.model.ShippingGeo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {ApplicationContextProvider.class}))
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CustomShippingGeoMongoRepositoryImplTest {

    private static ShippingGeoMongoRepository repository;

    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final Long ID_3 = 3L;
    private static final String RF = "RF";
    private static final String BR = "BR";
    private static final String MOS = "Moscow";
    private static final String SAR = "Saratov";
    private static final String BAL = "Balakovo";
    private static final String MNK = "Minsk";
    private static final LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);
    private static final int YEARS = 5;

    @BeforeAll
    static void beforeAll(@Autowired ApplicationContextProvider contextProvider) {
        ApplicationContext context = ApplicationContextProvider.getContext();
        assert context != null : "(ApplicationContext) context is null";

        repository = context.getBean(ShippingGeoMongoRepository.class);

        ShippingGeo geo = new ShippingGeo()
                .setItemId(ID_1)
                .setCountry(RF)
                .setCity(MOS)
                .setTimestamp(dateTime.minusYears(YEARS));

        ShippingGeo geo0 = new ShippingGeo()
                .setItemId(ID_2)
                .setCountry(RF)
                .setCity(MOS)
                .setTimestamp(dateTime.minusYears(YEARS));

        ShippingGeo geo1 = new ShippingGeo()
                .setItemId(ID_2)
                .setCountry(RF)
                .setCity(BAL)
                .setTimestamp(dateTime.minusYears(YEARS));

        ShippingGeo geo2 = new ShippingGeo()
                .setItemId(ID_3)
                .setCountry(RF)
                .setCity(SAR)
                .setTimestamp(dateTime);

        ShippingGeo geo3 = new ShippingGeo()
                .setItemId(ID_3)
                .setCountry(RF)
                .setCity(BAL)
                .setTimestamp(dateTime);

        ShippingGeo geo4 = new ShippingGeo()
                .setItemId(ID_3)
                .setCountry(BR)
                .setCity(MNK)
                .setTimestamp(dateTime);

        repository.saveAll(List.of(
                geo, geo, geo0, geo1, geo2, geo3, geo4
        )).collectList().block();
    }

    @AfterAll
    static void afterAll() {
        repository.deleteAll().block();
    }

    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeUniqueValue")
    void findShippingCountryCities_shouldRightListingForCountriesAndCities_whenUniqueIsFalseOrNull(
            String name, Boolean unique
    ) {
        ShippingGeoSearchFilter filter = new ShippingGeoSearchFilter().setNullFieldsToDefault();
        filter.setUnique(unique);
        filter.setSortDirection(SortDirection.DESC.name());

        List<ShippingGeoDtoResponse> responses = repository.findShippingCountryCities(filter).collectList().block();

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(3);

        ShippingGeoDtoResponse response0 = responses.get(0);
        ShippingGeoDtoResponse response1 = responses.get(1);
        ShippingGeoDtoResponse response2 = responses.get(2);

        assertThat(response0.getCountryCount()).isEqualTo(2);
        assertThat(response0.getCityCount()).isEqualTo(3);
        assertThat(response0.getCountryCount()).isEqualTo(response0.getCountryCities().size());
        assertThat(response0.getCountryCities().get(BR)).isEqualTo(List.of(MNK));
        assertThat(response0.getCountryCities().get(RF)).isEqualTo(List.of(SAR, BAL));

        assertThat(response1.getCountryCount()).isEqualTo(1);
        assertThat(response1.getCityCount()).isEqualTo(2);
        assertThat(response1.getCountryCount()).isEqualTo(response1.getCountryCities().size());
        assertThat(response1.getCountryCities().get(RF)).isEqualTo(List.of(MOS, BAL));

        assertThat(response2.getCountryCount()).isEqualTo(1);
        assertThat(response2.getCityCount()).isEqualTo(2);
        assertThat(response2.getCountryCount()).isEqualTo(response2.getCountryCities().size());
        assertThat(response2.getCountryCities().get(RF)).isEqualTo(List.of(MOS, MOS));
    }

    @Test
    void findShippingCountryCities_shouldRightListingForCountriesAndCities_whenUniqueIsTrue() {
        ShippingGeoSearchFilter filter = new ShippingGeoSearchFilter().setNullFieldsToDefault();
        filter.setUnique(true);
        filter.setSortDirection(SortDirection.DESC.name());

        List<ShippingGeoDtoResponse> responses = repository.findShippingCountryCities(filter).collectList().block();

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(3);

        ShippingGeoDtoResponse response0 = responses.get(0);
        ShippingGeoDtoResponse response1 = responses.get(1);
        ShippingGeoDtoResponse response2 = responses.get(2);

        assertThat(response0.getCountryCount()).isEqualTo(2);
        assertThat(response0.getCityCount()).isEqualTo(3);
        assertThat(response0.getCountryCount()).isEqualTo(response0.getCountryCities().size());
        assertThat(response0.getCountryCities().get(BR)).isEqualTo(List.of(MNK));
        assertThat(response0.getCountryCities().get(RF)).containsExactlyInAnyOrder(SAR, BAL);

        assertThat(response1.getCountryCount()).isEqualTo(1);
        assertThat(response1.getCityCount()).isEqualTo(2);
        assertThat(response1.getCountryCount()).isEqualTo(response1.getCountryCities().size());
        assertThat(response1.getCountryCities().get(RF)).containsExactlyInAnyOrder(MOS, BAL);

        assertThat(response2.getCountryCount()).isEqualTo(1);
        assertThat(response2.getCityCount()).isEqualTo(1);
        assertThat(response2.getCountryCount()).isEqualTo(response2.getCountryCities().size());
        assertThat(response2.getCountryCities().get(RF)).isEqualTo(List.of(MOS));
    }

    @Test
    void findShippingCountryCities_shouldRightListingForCountriesAndCities_whenInDateRange() {
        ShippingGeoSearchFilter filter = new ShippingGeoSearchFilter().setNullFieldsToDefault();
        filter.setStart(dateTime.minusYears(YEARS));
        filter.setEnd(dateTime.minusYears(YEARS));
        filter.setSortDirection(SortDirection.DESC.name());

        List<ShippingGeoDtoResponse> responses = repository.findShippingCountryCities(filter).collectList().block();

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2);

        ShippingGeoDtoResponse response0 = responses.get(0);
        ShippingGeoDtoResponse response1 = responses.get(1);

        assertThat(response0.getCountryCount()).isEqualTo(1);
        assertThat(response0.getCityCount()).isEqualTo(2);
        assertThat(response0.getCountryCount()).isEqualTo(response0.getCountryCities().size());
        assertThat(response0.getCountryCities().get(RF)).isEqualTo(List.of(MOS, BAL));

        assertThat(response1.getCountryCount()).isEqualTo(1);
        assertThat(response1.getCityCount()).isEqualTo(2);
        assertThat(response1.getCountryCount()).isEqualTo(response1.getCountryCities().size());
        assertThat(response1.getCountryCities().get(RF)).isEqualTo(List.of(MOS, MOS));
    }

    @Test
    void findShippingCountryCities_shouldRightListingForCountriesAndCities_whenSpecifiedItemIds() {
        ShippingGeoSearchFilter filter = new ShippingGeoSearchFilter().setNullFieldsToDefault();
        filter.setItemIds(List.of(ID_1, ID_3));
        filter.setSortDirection(SortDirection.DESC.name());

        List<ShippingGeoDtoResponse> responses = repository.findShippingCountryCities(filter).collectList().block();

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2);

        ShippingGeoDtoResponse response0 = responses.get(0);
        ShippingGeoDtoResponse response1 = responses.get(1);

        assertThat(response0.getCountryCount()).isEqualTo(2);
        assertThat(response0.getCityCount()).isEqualTo(3);
        assertThat(response0.getCountryCount()).isEqualTo(response0.getCountryCities().size());
        assertThat(response0.getCountryCities().get(RF)).isEqualTo(List.of(SAR, BAL));
        assertThat(response0.getCountryCities().get(BR)).isEqualTo(List.of(MNK));

        assertThat(response1.getCountryCount()).isEqualTo(1);
        assertThat(response1.getCityCount()).isEqualTo(2);
        assertThat(response1.getCountryCount()).isEqualTo(response1.getCountryCities().size());
        assertThat(response1.getCountryCities().get(RF)).isEqualTo(List.of(MOS, MOS));
    }

    @Test
    void findShippingCountryCities_shouldRightListingForCountriesAndCities_whenSpecifiedCountries() {
        ShippingGeoSearchFilter filter = new ShippingGeoSearchFilter().setNullFieldsToDefault();
        filter.setCountries(List.of(RF));
        filter.setSortDirection(SortDirection.DESC.name());

        List<ShippingGeoDtoResponse> responses = repository.findShippingCountryCities(filter).collectList().block();

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(3);

        ShippingGeoDtoResponse response0 = responses.get(0);
        ShippingGeoDtoResponse response1 = responses.get(1);
        ShippingGeoDtoResponse response2 = responses.get(2);

        assertThat(response0.getCountryCount()).isEqualTo(1);
        assertThat(response0.getCityCount()).isEqualTo(2);
        assertThat(response0.getCountryCount()).isEqualTo(response0.getCountryCities().size());
        assertThat(response0.getCountryCities().get(RF)).isEqualTo(List.of(SAR, BAL));

        assertThat(response1.getCountryCount()).isEqualTo(1);
        assertThat(response1.getCityCount()).isEqualTo(2);
        assertThat(response1.getCountryCount()).isEqualTo(response1.getCountryCities().size());
        assertThat(response1.getCountryCities().get(RF)).isEqualTo(List.of(MOS, BAL));

        assertThat(response2.getCountryCount()).isEqualTo(1);
        assertThat(response2.getCityCount()).isEqualTo(2);
        assertThat(response2.getCountryCount()).isEqualTo(response2.getCountryCities().size());
        assertThat(response2.getCountryCities().get(RF)).isEqualTo(List.of(MOS, MOS));
    }

    @Test
    void findShippingCountryCities_shouldReturnRightDtos_whenSpecifiedPagination() {
        ShippingGeoSearchFilter filter = new ShippingGeoSearchFilter().setNullFieldsToDefault();
        filter.setUnique(false);
        filter.setLastCityCount(2);
        filter.setLastCountryCount(1);
        filter.setLastItemId(2L);
        filter.setPageSize(10);
        filter.setSortDirection(SortDirection.DESC.name());

        List<ShippingGeoDtoResponse> responses = repository.findShippingCountryCities(filter).collectList().block();

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(1);

        ShippingGeoDtoResponse response0 = responses.get(0);

        assertThat(response0.getCountryCount()).isEqualTo(1);
        assertThat(response0.getCityCount()).isEqualTo(2);
        assertThat(response0.getCountryCount()).isEqualTo(response0.getCountryCities().size());
        assertThat(response0.getCountryCities().get(RF)).isEqualTo(List.of(MOS, MOS));
    }

    public static Stream<Arguments> makeUniqueValue() {
        return Stream.of(
                Arguments.of("unique_is_null", null),
                Arguments.of("unique_is_false", false)
        );
    }
}