package ru.pavbatol.myplace.app.pagination;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import ru.pavbatol.myplace.shared.dto.pagination.Paged;
import ru.pavbatol.myplace.shared.dto.pagination.SimplePage;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageConverter {

    public static <T> SimplePage<T> toSimplePage(Page<T> page) {
        List<T> content = page.getContent();
        return createPaged(page, content);
    }

    public static <T, R> SimplePage<R> toSimplePage(Page<T> page, Function<? super T, ? extends R> elementConverter) {
        List<R> content = page.stream().map(elementConverter).collect(Collectors.toList());
        return createPaged(page, content);
    }

    private static <T> Paged<T> createPaged(Page<?> page, List<T> content) {
        return new Paged<T>()
                .setContent(content)
                .setSize(page.getSize())
                .setNumberOfElements(page.getNumberOfElements())
                .setHasNext(page.hasNext())
                .setNumber(page.getNumber())
                .setTotalElements(page.getTotalElements())
                .setTotalPages(page.getTotalPages());
    }
}
