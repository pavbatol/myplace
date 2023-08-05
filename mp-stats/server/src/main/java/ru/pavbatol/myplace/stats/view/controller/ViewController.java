package ru.pavbatol.myplace.stats.view.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pavbatol.myplace.dto.view.ViewDtoAddRequest;
import ru.pavbatol.myplace.dto.view.ViewDtoAddResponse;
import ru.pavbatol.myplace.dto.view.ViewDtoResponse;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;
import ru.pavbatol.myplace.stats.view.service.ViewService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
@Tag(name = "Views", description = "API for working with 'View' entity")
public class ViewController {

    private final ViewService viewService;

    @PostMapping("/views")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add", description = "adding a View")
    public Mono<ViewDtoAddResponse> add(@Valid @RequestBody ViewDtoAddRequest dto) {
        log.debug("POST (add) with dto={},", dto);
        return viewService.add(dto);
    }

    @GetMapping("/views")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "find", description = "finding Views by filter")
    public Flux<ViewDtoResponse> find(@Valid ViewSearchFilter filter) {
        log.debug("GET (find) with filter={}", filter);
        return viewService.find(filter);
    }
}
