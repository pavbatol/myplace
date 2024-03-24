package ru.pavbatol.myplace.server.cart.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
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
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddRequest;
import ru.pavbatol.myplace.dto.cart.CartItemSearchFilter;
import ru.pavbatol.myplace.dto.cart.UserCartItemSearchFilter;
import ru.pavbatol.myplace.server.cart.mapper.CartItemMapper;
import ru.pavbatol.myplace.server.cart.model.CartItem;
import ru.pavbatol.myplace.server.cart.repository.CartItemMongoRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class CartItemServiceImplTest {
    @Mock
    private CartItemMongoRepository repository;
    private final CartItemMapper mapper = Mappers.getMapper(CartItemMapper.class);
    private CartItemServiceImpl service;

    @Captor
    private ArgumentCaptor<CartItem> cartItemArgumentCaptor;

    @BeforeEach
    void setUp() {
        service = new CartItemServiceImpl(repository, mapper);
    }

    @Test
    void add_shouldCorrectMappingAndPresetFieldValuesAndInvokeRepoAndAdded() {
        Long userId = 1L;
        Long itemId = 4L;
        LocalDateTime timestamp = LocalDateTime.now();

        CartItemDtoAddRequest addRequest = CartItemDtoAddRequest.builder()
                .userId(userId)
                .itemId(itemId)
                .timestamp(timestamp)
                .build();

        CartItem addedCartItem = new CartItem()
                .setId(null)
                .setUserId(userId)
                .setItemId(itemId)
                .setTimestamp(timestamp);

        Mockito.when(repository.save(any())).thenReturn(Mono.just(addedCartItem));

        service.add(addRequest);

        Mockito.verify(repository, Mockito.times(1)).save(cartItemArgumentCaptor.capture());

        CartItem captorValue = cartItemArgumentCaptor.getValue();

        assertAll(
                () -> Assertions.assertNull(captorValue.getId()),
                () -> Assertions.assertEquals(userId, captorValue.getUserId()),
                () -> Assertions.assertEquals(itemId, captorValue.getItemId()),
                () -> Assertions.assertEquals(timestamp, captorValue.getTimestamp())
        );
    }

    @Test
    void add_shouldDateTimeNotNullWenItReceivedAsNull() {
        Mockito.when(repository.save(any())).thenReturn(Mono.empty());

        service.add(new CartItemDtoAddRequest());

        verify(repository, times(1)).save(cartItemArgumentCaptor.capture());

        CartItem captorValue = cartItemArgumentCaptor.getValue();
        assertNotNull(captorValue.getTimestamp());
    }

    @Test
    void find_shouldFilterInvokeSetNullFieldsToDefault_andRepoInvoked() {
        CartItemSearchFilter filter = Mockito.mock(CartItemSearchFilter.class);

        Mockito.when(filter.setNullFieldsToDefault()).thenReturn(filter);

        service.find(filter);

        Mockito.verify(filter, Mockito.times(1)).setNullFieldsToDefault();

        Mockito.verify(repository, Mockito.times(1)).find(filter);
    }

    @Test
    void findUserCartItems_shouldFilterInvokeSetNullFieldsToDefault_andRepoInvoked() {
        UserCartItemSearchFilter filter = Mockito.mock(UserCartItemSearchFilter.class);

        Mockito.when(filter.setNullFieldsToDefault()).thenReturn(filter);

        service.findUserCartItems(filter);

        Mockito.verify(filter, Mockito.times(1)).setNullFieldsToDefault();

        Mockito.verify(repository, Mockito.times(1)).findUserCartItems(filter);
    }
}