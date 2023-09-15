package ru.pavbatol.myplace.server.cart.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.cart.CartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.CartItemSearchFilter;
import ru.pavbatol.myplace.dto.cart.UserCartItemDtoResponse;
import ru.pavbatol.myplace.dto.cart.UserCartItemSearchFilter;
import ru.pavbatol.myplace.server.CustomDataMongoTest;
import ru.pavbatol.myplace.server.app.context.ApplicationContextProvider;
import ru.pavbatol.myplace.server.cart.model.CartItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@CustomDataMongoTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CustomCartItemMongoRepositoryImplTest {

    private static CartItemMongoRepository repository;

    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final Long ID_3 = 3L;
    private static final LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

    @BeforeAll
    static void beforeAll(@Autowired ApplicationContextProvider contextProvider) {
        ApplicationContext context = ApplicationContextProvider.getContext();
        assert context != null : "(ApplicationContext) context is null";

        repository = context.getBean(CartItemMongoRepository.class);

        repository.deleteAll().block();

        CartItem cartItem = new CartItem()
                .setUserId(ID_1)
                .setItemId(ID_1)
                .setTimestamp(dateTime);

        CartItem cartItem0 = new CartItem()
                .setUserId(ID_1)
                .setItemId(ID_2)
                .setTimestamp(dateTime);

        CartItem cartItem1 = new CartItem()
                .setUserId(ID_1)
                .setItemId(ID_3)
                .setTimestamp(dateTime);

        CartItem cartItem2 = new CartItem()
                .setUserId(ID_2)
                .setItemId(ID_1)
                .setTimestamp(dateTime);

        CartItem cartItem3 = new CartItem()
                .setUserId(ID_2)
                .setItemId(ID_2)
                .setTimestamp(dateTime);

        CartItem cartItem4 = new CartItem()
                .setUserId(ID_3)
                .setItemId(ID_1)
                .setTimestamp(dateTime);

        repository.saveAll(List.of(
                cartItem,
                cartItem,
                cartItem0,
                cartItem1,
                cartItem2,
                cartItem3,
                cartItem4)).collectList().block();
    }

    @AfterAll
    static void afterAll() {
        repository.deleteAll().block();
    }

    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeUniqueValue")
    void find_shouldReturnNotUniqueCounting_whenUniqueIsFalseOrNull(String name, Boolean unique) {
        CartItemSearchFilter filter = new CartItemSearchFilter().setNullFieldsToDefault();
        filter.setUnique(unique);
        filter.setSortDirection(SortDirection.DESC.name());

        List<CartItemDtoResponse> responses = repository.find(filter).collectList().block();

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(3);

        assertThat(responses.get(0).getItemId()).as("Checking the 'itemId' value").isEqualTo(ID_1);
        assertThat(responses.get(0).getCartItemCount()).as("Checking the CartItemCount value").isEqualTo(4);

        assertThat(responses.get(1).getItemId()).as("Checking the 'itemId' value").isEqualTo(ID_2);
        assertThat(responses.get(1).getCartItemCount()).as("Checking the CartItemCount value").isEqualTo(2);

        assertThat(responses.get(2).getItemId()).as("Checking the 'itemId' value").isEqualTo(ID_3);
        assertThat(responses.get(2).getCartItemCount()).as("Checking the 'cartItemCount' value").isEqualTo(1);
    }

    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("makeUniqueValue")
    void findUserCartItems_shouldReturnNotUniqueCounting_whenUniqueIsFalseOrNull(String name, Boolean unique) {
        UserCartItemSearchFilter filter = new UserCartItemSearchFilter().setNullFieldsToDefault();
        filter.setUnique(unique);
        filter.setSortDirection(SortDirection.DESC.name());

        List<UserCartItemDtoResponse> responses = repository.findUserCartItems(filter).collectList().block();

        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(3);

        assertThat(responses.get(0).getUserId()).as("Checking the 'userId' value").isEqualTo(ID_1);
        assertThat(responses.get(0).getCartItemIds()).hasSize(4);
        assertThat(responses.get(0).getItemCount()).as("Checking the 'itemCount' value").isEqualTo(4);

        assertThat(responses.get(1).getUserId()).as("Checking the 'userId' value").isEqualTo(ID_2);
        assertThat(responses.get(1).getCartItemIds()).hasSize(2);
        assertThat(responses.get(1).getItemCount()).as("Checking the 'itemCount' value").isEqualTo(2);

        assertThat(responses.get(2).getUserId()).as("Checking the 'userId' value").isEqualTo(ID_3);
        assertThat(responses.get(2).getCartItemIds()).hasSize(1);
        assertThat(responses.get(2).getItemCount()).as("Checking the 'itemCount' value").isEqualTo(1);
    }

    private static Stream<Arguments> makeUniqueValue() {
        return Stream.of(
                Arguments.of("unique_is_null", null),
                Arguments.of("unique_is_false", false)
        );
    }
}