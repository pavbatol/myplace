package ru.pavbatol.myplace.shared.dto.pagination;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class PageDto<T> implements SimplePage<T> {
    List<T> content;
    int page;
    int size;
    long totalElements;
    int totalPage;
}
