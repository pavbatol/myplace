package ru.pavbatol.myplace.app.pagination;

import lombok.Value;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Value
public class SliceDto<T> {
    List<T> content;
    int size;
    int numberOfElements;
    boolean hasNext;

    public static <E, R> SliceDto<R> from(Slice<E> slice, Function<E, R> elementConvertor) {
        return new SliceDto<R>(
                slice.stream().map(elementConvertor).collect(Collectors.toList()),
                slice.getSize(),
                slice.getNumberOfElements(),
                slice.hasNext()
        );
    }
}
