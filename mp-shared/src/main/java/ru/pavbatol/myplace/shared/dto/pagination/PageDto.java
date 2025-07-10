package ru.pavbatol.myplace.shared.dto.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Value
@Builder
@Jacksonized
public class PageDto<T> implements SimplePage<T> {
    List<T> content;

    Integer number;

    Integer size;

    Long totalElements;

    Integer totalPages;

    public static <T, R> PageDto<R> from(Page<T> page, Function<T, R> elementConverter) {
        return PageDto.<R>builder()
                .content(page.stream().map(elementConverter).collect(Collectors.toList()))
                .number(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
