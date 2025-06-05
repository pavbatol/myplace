package ru.pavbatol.myplace.geo.common.pagination;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import ru.pavbatol.myplace.app.util.SqlUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract repository implementation for paging through geo entities.
 * Provides common functionality for KeySet Pagination and relation fetching.
 *
 * <p>The repository supports optional eager loading of nested entity relations specified via
 * a dot-separated path (e.g., "city.district.region"). When no relation path is provided,
 * only the root entity properties will be loaded.</p>
 *
 * @param <T> the type of geo entity this repository manages
 */
@Slf4j
public abstract class AbstractGeoEntityPagingRepository<T> implements GeoEntityPagingRepository<T> {
    public static final String DEFAULT_CURSOR_ATTRIBUTE = "name";
    private static final String ATTR_ID = "id";
    private static final char ESCAPE_CHAR = '!';

    private final String cursorField;
    private final String relationsFetchPath;
    private final Class<T> entityType;

    @PersistenceContext
    private EntityManager em;

    public AbstractGeoEntityPagingRepository(@NonNull String cursorField,
                                             @Nullable String relationsFetchPath,
                                             Class<T> entityType) {
        this.cursorField = cursorField;
        this.relationsFetchPath = relationsFetchPath;
        this.entityType = entityType;
    }

    public AbstractGeoEntityPagingRepository(@Nullable String relationsFetchPath,
                                             Class<T> entityType) {
        this(DEFAULT_CURSOR_ATTRIBUTE, relationsFetchPath, entityType);
    }

    /**
     * Finds a page of entities filtered by field values beginning with the given prefix (case-insensitive),
     * using KeySet Pagination.
     *
     * <p>The method filters entities where the designated cursor field starts with
     * the provided prefix string, ignoring case differences.
     *
     * <p>
     * The pagination is implemented using a combination of cursor field value and entity ID.
     *
     * @param cursorFieldStartWith the prefix to match against entity specified field
     * @param lastSeenCursorField  the cursor (first key) value of the last seen entity
     * @param lastSeenId           the ID of the last seen entity (second key)
     * @param size                 the maximum number of entities to return in the page
     * @return a {@link Slice} containing the requested page of entities
     */
    @Override
    public Slice<T> findPageByNamePrefixIgnoreCase(String cursorFieldStartWith, String lastSeenCursorField, Long lastSeenId, int size) {
        validatePaginationParams(lastSeenCursorField, lastSeenId, size);
        boolean firstPage = isFirstPage(lastSeenCursorField, lastSeenId);
        log.debug("Fetching {} for {}: cursorFieldStartWith={}, lastSeenCursorField={}, lastSeenId={}, size={}",
                firstPage ? "first page" : "next page", entityType.getSimpleName(), cursorFieldStartWith, lastSeenCursorField, lastSeenId, size);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityType);
        Root<T> root = query.from(entityType);

        fetchNested(root, relationsFetchPath);

        Predicate nameLikePredicate;
        if (!StringUtils.hasText(cursorFieldStartWith)) {
            nameLikePredicate = cb.conjunction();
        } else {
            nameLikePredicate = cb.like(
                    cb.lower(root.get(cursorField)),
                    cb.concat(SqlUtils.escapeSqlLikeWildcards(cursorFieldStartWith.toLowerCase(), ESCAPE_CHAR), cb.literal("%")),
                    ESCAPE_CHAR
            );
        }

        Predicate paginationPredicate = firstPage ? cb.conjunction() : cb.or(
                cb.greaterThan(root.get(cursorField), lastSeenCursorField),
                cb.and(
                        cb.equal(root.get(cursorField), lastSeenCursorField),
                        cb.greaterThan(root.get(ATTR_ID), lastSeenId)
                ));

        query.where(cb.and(nameLikePredicate, paginationPredicate));
        query.orderBy(cb.asc(root.get(cursorField)), cb.asc(root.get(ATTR_ID)));

        List<T> content = em.createQuery(query)
                .setMaxResults(size + 1)
                .getResultList();

        boolean hasNext = content.size() > size;

        return new SliceImpl<>(
                hasNext ? content.subList(0, size) : content,
                PageRequest.of(0, size, Sort.by(cursorField, ATTR_ID).ascending()),
                hasNext);
    }

    private void validatePaginationParams(String lastSeenCursorField, Long lastSeenId, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        if (isFirstPage(lastSeenCursorField, lastSeenId)) {
            return;
        }
        if (!StringUtils.hasText(lastSeenCursorField) || lastSeenId == null) {
            throw new IllegalArgumentException(String.format(
                    "For non-first page both lastSeenCursorField and lastSeenId must be provided but received lastSeenCursorField=%s and lastSeenId=%s",
                    lastSeenCursorField, lastSeenId));
        }
    }

    private boolean isFirstPage(String lastSeenCursorField, Long lastSeenId) {
        return lastSeenCursorField == null && lastSeenId == null;
    }

    /**
     * Eagerly fetches nested entity relations specified by the dot-separated path.
     * If the path is null or empty, this method does nothing.
     *
     * <p>This implementation uses JPA's fetch joins to load all specified relations
     * in a single query to avoid N+1 query problems.</p>
     *
     * @param from the root entity From object to start fetching from
     * @param path dot-separated path of relations to fetch (e.g., "city.district.region").
     *             May be null, in which case no fetching occurs.
     */
    private void fetchNested(From<?, ?> from, String path) {
        if (path == null) {
            return;
        }

        log.debug("Loading entity relations using path: {}", path);

        String[] parts = path.split("\\.");

        if (parts.length > 0) {
            log.debug("Detected relation path components: {}", Arrays.toString(parts));

            Fetch<?, ?> currentFetch = from.fetch(parts[0], JoinType.LEFT);
            for (int i = 1; i < parts.length; i++) {
                currentFetch = currentFetch.fetch(parts[i], JoinType.LEFT);
            }
        }
    }
}
