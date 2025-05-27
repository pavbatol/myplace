package ru.pavbatol.myplace.app.pagination;

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
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
public abstract class AbstractGeoEntityPagingRepository<T> implements GeoEntityPagingRepository<T> {
    private static final String ATTR_ID = "id";
    private static final char ESCAPE_CHAR = '!';

    private final String sortAttributeName;
    private final String relationsFetchPath;
    private final Class<T> entityType;
    private final EntityManager em;

    public AbstractGeoEntityPagingRepository(@NonNull String sortAttributeName,
                                             @Nullable String relationsFetchPath,
                                             Class<T> entityType,
                                             EntityManager em) {
        this.sortAttributeName = sortAttributeName;
        this.relationsFetchPath = relationsFetchPath;
        this.entityType = entityType;
        this.em = em;
    }

    @Override
    public Slice<T> findPageByNamePrefixIgnoreCase(String nameStartWith, String lastSeenName, Long lastSeenId, int size) {
        validatePaginationParams(lastSeenName, lastSeenId, size);
        boolean firstPage = isFirstPage(lastSeenName, lastSeenId);
        log.debug("Fetching {} for {}: prefix={}, lastSeen={}, lastSeenId={}, size={}",
                firstPage ? "first page" : "next page", entityType.getSimpleName(), nameStartWith, lastSeenName, lastSeenId, size);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityType);
        Root<T> root = query.from(entityType);

        fetchNested(root, relationsFetchPath);

        Predicate nameLikePredicate;
        if (!StringUtils.hasText(nameStartWith)) {
            nameLikePredicate = cb.conjunction();
        } else {
            nameLikePredicate = cb.like(
                    cb.lower(root.get(sortAttributeName)),
                    cb.concat(SqlUtils.escapeSqlLikeWildcards(nameStartWith, ESCAPE_CHAR), cb.literal("%")),
                    ESCAPE_CHAR
            );
        }

        Predicate paginationPredicate = firstPage ? cb.conjunction() : cb.or(
                cb.greaterThan(root.get(sortAttributeName), lastSeenName),
                cb.and(
                        cb.equal(root.get(sortAttributeName), lastSeenName),
                        cb.greaterThan(root.get(ATTR_ID), lastSeenId)
                ));

        query.where(cb.and(nameLikePredicate, paginationPredicate));
        query.orderBy(cb.asc(root.get(sortAttributeName)), cb.asc(root.get(ATTR_ID)));

        List<T> content = em.createQuery(query)
                .setMaxResults(size + 1)
                .getResultList();

        boolean hasNext = content.size() > size;

        return new SliceImpl<>(
                hasNext ? content.subList(0, size) : content,
                PageRequest.of(0, size, Sort.by(sortAttributeName, ATTR_ID).ascending()),
                hasNext);
    }

    private void validatePaginationParams(String lastSeenName, Long lastSeenId, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        if (isFirstPage(lastSeenName, lastSeenId)) {
            return;
        }
        if (!StringUtils.hasText(lastSeenName) || lastSeenId == null) {
            throw new IllegalArgumentException(String.format(
                    "For non-first page both lastSeenName and lastSeenId must be provided but received lastSeenName=%s and lastSeenId=%s",
                    lastSeenName, lastSeenId));
        }
    }

    private boolean isFirstPage(String lastSeenName, Long lastSeenId) {
        return lastSeenName == null && lastSeenId == null;
    }

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
