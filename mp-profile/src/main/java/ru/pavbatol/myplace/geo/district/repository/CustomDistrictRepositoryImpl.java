package ru.pavbatol.myplace.geo.district.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import ru.pavbatol.myplace.app.util.SqlUtils;
import ru.pavbatol.myplace.geo.district.model.District;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomDistrictRepositoryImpl implements CustomDistrictRepository {
    private static final String ATTR_NAME = "name";
    private static final String ATTR_ID = "id";
    private static final char ESCAPE_CHAR = '!';
    public static final String ENTITY_SIMPLE_NAME = District.class.getSimpleName();
    private final EntityManager em;

    @Override
    public Slice<District> findPageByNamePrefixIgnoreCase(String nameStartWith, String lastSeenName, Long lastSeenId, int size) {
        validatePaginationParams(lastSeenName, lastSeenId, size);
        boolean firstPage = isFirstPage(lastSeenName, lastSeenId);
        log.debug("Fetching {} for {}: prefix={}, lastSeen={}, lastSeenId={}, size={}",
                firstPage ? "first page" : "next page", ENTITY_SIMPLE_NAME, nameStartWith, lastSeenName, lastSeenId, size);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<District> query = cb.createQuery(District.class);
        Root<District> root = query.from(District.class);
        root.fetch("region", JoinType.LEFT).fetch("country", JoinType.LEFT);

        Predicate nameLikePredicate;
        if (!StringUtils.hasText(nameStartWith)) {
            nameLikePredicate = cb.conjunction();
        } else {
            nameLikePredicate = cb.like(
                    cb.lower(root.get(ATTR_NAME)),
                    cb.concat(SqlUtils.escapeSqlLikeWildcards(nameStartWith, ESCAPE_CHAR), cb.literal("%")),
                    ESCAPE_CHAR
            );
        }

        Predicate paginationPredicate = firstPage ? cb.conjunction() : cb.or(
                cb.greaterThan(root.get(ATTR_NAME), lastSeenName),
                cb.and(
                        cb.equal(root.get(ATTR_NAME), lastSeenName),
                        cb.greaterThan(root.get(ATTR_ID), lastSeenId)
                ));

        query.where(cb.and(nameLikePredicate, paginationPredicate));
        query.orderBy(cb.asc(root.get(ATTR_NAME)), cb.asc(root.get(ATTR_ID)));

        List<District> content = em.createQuery(query)
                .setMaxResults(size + 1)
                .getResultList();

        boolean hasNext = content.size() > size;

        return new SliceImpl<>(
                hasNext ? content.subList(0, size) : content,
                PageRequest.of(0, size, Sort.by(ATTR_NAME, ATTR_ID).ascending()),
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
            throw new IllegalArgumentException(
                    "For non-first page, both lastSeenName and lastSeenId must be provided"
            );
        }
    }

    private boolean isFirstPage(String lastSeenName, Long lastSeenId) {
        return lastSeenName == null && lastSeenId == null;
    }
}
