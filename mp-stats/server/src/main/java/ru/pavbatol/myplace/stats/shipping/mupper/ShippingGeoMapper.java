package ru.pavbatol.myplace.stats.shipping.mupper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddRequest;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoAddResponse;
import ru.pavbatol.myplace.stats.shipping.model.ShippingGeo;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ShippingGeoMapper {
    ShippingGeo toEntity(ShippingGeoDtoAddRequest dto);

    ShippingGeoDtoAddResponse toDtoAddResponse(ShippingGeo shippingGeo);
}
