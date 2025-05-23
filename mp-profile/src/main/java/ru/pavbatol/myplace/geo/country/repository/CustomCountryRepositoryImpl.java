package ru.pavbatol.myplace.geo.country.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import ru.pavbatol.myplace.app.util.SqlUtils;
import ru.pavbatol.myplace.geo.country.model.Country;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomCountryRepositoryImpl implements CustomCountryRepository {
    private static final char ESCAPE_CHAR = '!';
    private static final Sort DEFAULT_SORT = Sort.by("name").ascending();

    @PersistenceContext
    private final EntityManager em;

    /**
     * Finds next page of countries alphabetically after {@code lastSeenName}.
     * Results are ordered by name ASC and limited to {@code size} items.
     *
     * @param lastSeenName country name to start after (exclusive, null for first page)
     * @param size         page size (must be > 0)
     * @return Slice with countries and pagination info
     * @throws IllegalArgumentException if size <= 0
     */
    @Override
    @Transactional(readOnly = true)
    public Slice<Country> findNextPage(String lastSeenName, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }

        log.debug("Finding next page with lastSeenName: {}, size: {}", lastSeenName, size);

        String sql = "SELECT c FROM Country c " +
                "WHERE :lastSeenName IS NULL OR c.name > :lastSeenName " +
                "ORDER BY c.name ASC ";

        TypedQuery<Country> query = em.createQuery(sql, Country.class);
        query.setParameter("lastSeenName", lastSeenName);
        query.setFirstResult(0);
        query.setMaxResults(size + 1);

        List<Country> content = query.getResultList();

        boolean hasNext = content.size() > size;
        if (hasNext) {
            content = content.subList(0, size);
        }

        return new SliceImpl<>(content, PageRequest.of(0, size, DEFAULT_SORT), hasNext);
    }

    /**
     * Finds countries by name prefix using keyset pagination.
     *
     * <p>Returns a {@link Slice} of countries whose names:
     * <ul>
     *   <li>Start with {@code nameStartWith} (case-insensitive, null ignores filter)</li>
     *   <li>Come after {@code lastSeenName} in alphabetical order (null starts from beginning)</li>
     * </ul>
     *
     * @param nameStartWith name prefix filter (optional)
     * @param lastSeenName  pagination anchor (exclusive)
     * @param size          maximum results per page (must be positive)
     * @return paginated results with sort metadata
     * @throws IllegalArgumentException               if size ≤ 0
     * @throws javax.persistence.PersistenceException if query fails
     * @implSpec Uses SQL-level escaping via {@link #ESCAPE_CHAR}
     */
    @Override
    @Transactional(readOnly = true)
    public Slice<Country> findNextPageByNamePrefixIgnoreCase(String nameStartWith, String lastSeenName, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }

        log.debug("Finding next page with nameStartWith: {}, lastSeenName: {}, size: {}", nameStartWith, lastSeenName, size);


        String sql = "SELECT c FROM Country c " +
                "WHERE (:nameStartWith IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT(:nameStartWith, '%' )) ESCAPE :escapeChar) " +
                "AND (:lastSeenName IS NULL OR c.name > :lastSeenName) " +
                "ORDER BY c.name ASC";

        TypedQuery<Country> query = em.createQuery(sql, Country.class)
                .setParameter("escapeChar", String.valueOf(ESCAPE_CHAR))
                .setParameter("nameStartWith", SqlUtils.escapeSqlLikeWildcards(nameStartWith))
                .setParameter("lastSeenName", lastSeenName)
                .setMaxResults(size + 1);

        List<Country> content = query.getResultList();

        boolean hasNext = content.size() > size;
        if (hasNext) {
            content = content.subList(0, size);
        }

        return new SliceImpl<>(content, PageRequest.of(0, size, DEFAULT_SORT), hasNext);
    }
}
