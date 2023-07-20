package ru.pavbatol.myplace.stats.view.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.stats.view.model.View;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ViewMapper {
    View toDocument(ViewDtoAddRequest dtoRequest);

    ViewDtoAddResponse toDtoResponse(View view);
}
