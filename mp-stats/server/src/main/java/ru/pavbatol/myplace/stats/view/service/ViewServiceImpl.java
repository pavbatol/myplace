package ru.pavbatol.myplace.stats.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
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
        View document = mapper.toDocument(dto);
        LocalDateTime dateTime = document.getTimestamp() == null
                ? LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
                : document.getTimestamp().truncatedTo(ChronoUnit.SECONDS);
        document.setTimestamp(dateTime);
        return repository.save(document).map(mapper::toDtoResponse);
    }

    @Override
    public List<ViewDtoResponse> find(String start, String end, List<String> uris, Boolean unique) {
        return null;
    }
}
