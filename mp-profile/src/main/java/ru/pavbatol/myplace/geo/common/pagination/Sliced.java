package ru.pavbatol.myplace.geo.common.pagination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Value
public class Sliced<T> implements SimpleSlice<T> {
    List<T> content;
    int size;
    int numberOfElements;
    @Getter(AccessLevel.NONE)
    boolean hasNext;

    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    public static <S, R> Sliced<R> from(Slice<S> slice, Function<S, R> elementConverter) {
        return new Sliced<R>(
                slice.stream().map(elementConverter).collect(Collectors.toList()),
                slice.getSize(),
                slice.getNumberOfElements(),
                slice.hasNext()
        );
    }
}
