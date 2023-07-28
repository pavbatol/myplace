package ru.pavbatol.myplace.stats.cart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddResponse;
import ru.pavbatol.myplace.dto.cart.CartItemDtoAddRequest;
import ru.pavbatol.myplace.stats.cart.model.CartItem;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CartItemMapper {

    CartItem toEntity(CartItemDtoAddRequest dto);

    CartItemDtoAddResponse toAddResponse(CartItem cartItem);
}
