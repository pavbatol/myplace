package ru.pavbatol.myplace.stats.view.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.SortDirection;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;
import ru.pavbatol.myplace.stats.view.mapper.ViewMapper;
import ru.pavbatol.myplace.stats.view.model.View;
import ru.pavbatol.myplace.stats.view.repository.ViewMongoRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewServiceImpl implements ViewService {
    private final ViewMongoRepository repository;
    private final ViewMapper mapper;

    @Override
    public Mono<ViewDtoAddResponse> add(ViewDtoAddRequest dto) {
        View entity = mapper.toEntity(dto);
        LocalDateTime dateTime = entity.getTimestamp() == null
                ? LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
                : entity.getTimestamp().truncatedTo(ChronoUnit.SECONDS);
        entity.setTimestamp(dateTime);
        return repository.save(entity).map(mapper::toDtoAddResponse);
    }

    @Override
    public Flux<ViewDtoResponse> find(@NonNull ViewSearchFilter filter) {
        filter
                .setStart(filter.getStart() != null ? filter.getStart() : LocalDateTime.of(1970, 1, 1, 0, 0, 0))
                .setEnd(filter.getEnd() != null ? filter.getEnd() : LocalDateTime.now())
                .setUris(filter.getUris() != null ? filter.getUris() : List.of())
                .setUnique(filter.getUnique() != null ? filter.getUnique() : false)
                .setSortDirection(filter.getSortDirection() != null ? filter.getSortDirection().name() : SortDirection.DESC.name());
        return repository.find(filter);
    }
}
