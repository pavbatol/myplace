package ru.pavbatol.myplace.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.data.repository.CrudRepository;
import ru.pavbatol.myplace.app.exception.NotFoundException;

import javax.validation.constraints.NotNull;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Checker {
    public static final String S_WITH_ID_S_WAS_NOT_FOUND = "%s with id=%s was not found";

    @NotNull
    public static <T, I> T getNonNullObject(@NotNull CrudRepository<T, I> storage, I id) throws NotFoundException {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(S_WITH_ID_S_WAS_NOT_FOUND, getTClass(storage).getSimpleName(), id)));
    }

    public static <T, I> void checkId(@NotNull CrudRepository<T, I> storage, I id) throws NotFoundException {
        if (!storage.existsById(id)) {
            throw new NotFoundException(String.format(S_WITH_ID_S_WAS_NOT_FOUND, getTClass(storage).getSimpleName(), id));
        }
    }

    private static <T, I> Class<T> getTClass(CrudRepository<T, I> storage) {
        ResolvableType resolvableType = ResolvableType.forClass(storage.getClass()).as(CrudRepository.class);
        return (Class<T>) resolvableType.getGeneric(0).toClass();
    }
}
