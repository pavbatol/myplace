package ru.pavbatol.myplace.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.lang.NonNull;
import ru.pavbatol.myplace.app.exception.NotFoundException;

import javax.validation.constraints.NotNull;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Checker {
    public static final String S_WITH_ID_S_WAS_NOT_FOUND = "%s with id=%s was not found";

    /**
     * Retrieves an entity by ID or throws NotFoundException if not found.
     *
     * @param repository the repository to search in (not null)
     * @param id         the entity ID to find
     * @param <T>        the entity type
     * @param <I>        the ID type
     * @return the found entity (never null)
     * @throws NotFoundException if entity doesn't exist
     */
    @NotNull
    public static <T, I> T getNonNullObject(@NotNull CrudRepository<T, I> repository, I id) throws NotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(S_WITH_ID_S_WAS_NOT_FOUND, getTClassSimpleName(repository), id)));
    }

    /**
     * Verifies an entity exists by ID or throws NotFoundException.
     *
     * @param repository the repository to check in (not null)
     * @param id         the entity ID to verify
     * @param <T>        the entity type
     * @param <I>        the ID type
     * @throws NotFoundException if entity doesn't exist
     */
    public static <T, I> void checkId(@NotNull CrudRepository<T, I> repository, I id) throws NotFoundException {
        if (!repository.existsById(id)) {
            throw new NotFoundException(String.format(S_WITH_ID_S_WAS_NOT_FOUND, getTClassSimpleName(repository), id));
        }
    }

    /**
     * Returns the simple name of the entity class for the given repository.
     * If the class cannot be determined through standard methods, returns a generated name
     * based on the repository class name (removing "Repository" suffix and adding "Entity").
     *
     * @param repository the repository instance (must not be null)
     * @param <T>        the entity type
     * @param <I>        the ID type
     * @return the simple class name of the entity (e.g., "User") or generated name (e.g., "UserEntity")
     */
    private static <T, I> String getTClassSimpleName(@NonNull CrudRepository<T, I> repository) {
        if (repository instanceof RepositoryInformation) {
            Class<?> domainType = ((RepositoryInformation) repository).getDomainType();
            return domainType.getSimpleName();
        }

        try {
            Class<?> resolved = ResolvableType.forInstance(repository)
                    .as(CrudRepository.class)
                    .getGeneric(0)
                    .resolve();
            if (resolved != null) {
                return resolved.getSimpleName();
            }
        } catch (Exception ex) {
            log.debug("Failed to resolve entity type for repository {}: {}", repository.getClass().getName(), ex.getMessage());
        }

        return extractEntityNameFromInterface(repository);
    }

    /**
     * Extracts entity name from repository interface (handles proxies).
     */
    private static <T, I> String extractEntityNameFromInterface(CrudRepository<T, I> repository) {
        Class<?>[] interfaces = repository.getClass().getInterfaces();
        for (Class<?> intf : interfaces) {
            if (intf != CrudRepository.class && CrudRepository.class.isAssignableFrom(intf)) {
                return intf.getSimpleName()
                        .replace("Repository", "")
                        .replace("Impl", "");
            }
        }
        return "UnknownEntity";
    }
}
