package ru.pavbatol.myplace.shared.dto.pagination;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class SliceDto<T> {
    List<T> content;

    Integer size;

    Integer numberOfElements;

    Boolean hasNext;
}
