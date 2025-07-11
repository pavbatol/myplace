package ru.pavbatol.myplace.app.pagination;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;
import ru.pavbatol.myplace.shared.dto.pagination.SimpleSlice;
import ru.pavbatol.myplace.shared.dto.pagination.Sliced;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SliceConverter {

    public static <T> SimpleSlice<T> toSimpleSlice(Slice<T> slice) {
        List<T> content = slice.getContent();
        return createPaged(slice, content);
    }

    public static <T, R> SimpleSlice<R> toSimpleSlice(Slice<T> slice, Function<? super T, ? extends R> elementConverter) {
        List<R> content = slice.stream().map(elementConverter).collect(Collectors.toList());
        return createPaged(slice, content);
    }

    private static <T> SimpleSlice<T> createPaged(Slice<?> slice, List<T> content) {
        return new Sliced<T>()
                .setContent(content)
                .setSize(slice.getSize())
                .setNumberOfElements(slice.getNumberOfElements())
                .setHasNext(slice.hasNext());
    }
}
