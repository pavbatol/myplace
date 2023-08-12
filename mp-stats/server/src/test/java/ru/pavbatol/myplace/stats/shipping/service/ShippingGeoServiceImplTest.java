package ru.pavbatol.myplace.stats.shipping.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddRequest;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoSearchFilter;
import ru.pavbatol.myplace.stats.shipping.model.ShippingGeo;
import ru.pavbatol.myplace.stats.shipping.mupper.ShippingGeoMapper;
import ru.pavbatol.myplace.stats.shipping.repository.ShippingGeoMongoRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShippingGeoServiceImplTest {

    @Mock
    private ShippingGeoMongoRepository repository;

    private final ShippingGeoMapper mapper = Mappers.getMapper(ShippingGeoMapper.class);

    private ShippingGeoServiceImpl service;

    @Captor
    private ArgumentCaptor<ShippingGeo> shippingGeoArgumentCaptor;

    @BeforeEach
    void setUp() {
        service = new ShippingGeoServiceImpl(repository, mapper);
    }

    @Test
    void add_shouldCorrectMappingAndInvokeRepo() {
        Long itemId = 1L;
        String country = "RF";
        String city = "Saratov";
        LocalDateTime timestamp = LocalDateTime.now();

        ShippingGeoDtoAddRequest addRequest = new ShippingGeoDtoAddRequest(itemId, country, city, timestamp);

        ShippingGeo addedShippingGeo = new ShippingGeo()
                .setId(null)
                .setItemId(itemId)
                .setCountry(country)
                .setCity(city)
                .setTimestamp(timestamp);

        when(repository.save(any())).thenReturn(Mono.just(addedShippingGeo));

        service.add(addRequest);

        verify(repository, times(1)).save(shippingGeoArgumentCaptor.capture());

        ShippingGeo captorValue = shippingGeoArgumentCaptor.getValue();

        assertAll(
                () -> assertNull(captorValue.getId()),
                () -> assertEquals(itemId, captorValue.getItemId()),
                () -> assertEquals(country, captorValue.getCountry()),
                () -> assertEquals(city, captorValue.getCity()),
                () -> assertEquals(timestamp, captorValue.getTimestamp())
        );
    }

    @Test
    void add_shouldDateTimeNotNullWenItReceivedAsNull() {
        when(repository.save(any())).thenReturn(Mono.empty());

        service.add(new ShippingGeoDtoAddRequest(null, null, null, null));

        verify(repository, times(1)).save(shippingGeoArgumentCaptor.capture());

        ShippingGeo captorValue = shippingGeoArgumentCaptor.getValue();
        assertNotNull(captorValue.getTimestamp());
    }

    @Test
    void findShippingCountryCities_shouldFilterInvokeSetNullFieldsToDefault_andRepoInvoked() {

        ShippingGeoSearchFilter filter = Mockito.mock(ShippingGeoSearchFilter.class);

        when(filter.setNullFieldsToDefault()).thenReturn(filter);

        service.findShippingCountryCities(filter);

        verify(filter, times(1)).setNullFieldsToDefault();

        verify(repository, times(1)).findShippingCountryCities(filter);
    }
}