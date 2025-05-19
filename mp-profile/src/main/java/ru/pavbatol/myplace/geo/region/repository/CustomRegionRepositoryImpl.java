package ru.pavbatol.myplace.geo.region.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import ru.pavbatol.myplace.geo.NameableGeo;
import ru.pavbatol.myplace.geo.country.model.Country;
import ru.pavbatol.myplace.geo.region.model.Region;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomRegionRepositoryImpl implements CustomRegionRepository {
    private static final char ESCAPE_CHAR = '!';
    private final EntityManager em;

    @Override
    public Slice<Region> findPageByNamePrefixIgnoreCase(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size) {
        validateParameters(lastSeenName, lastSeenCountryName, size);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Region> query = cb.createQuery(Region.class);
        Root<Region> region = query.from(Region.class);
        Join<Region, Country> country = region.join("country", JoinType.LEFT);

        EntityGraph<Region> regionEntityGraph = em.createEntityGraph(Region.class);
        regionEntityGraph.addAttributeNodes("country");

        Predicate prefixPredicate = buildNamePrefixPredicate(cb, region, nameStartWith);
        Predicate paginationPredicate = buildPaginationPredicate(cb, region, country, lastSeenName, lastSeenCountryName);

        query.where(cb.and(prefixPredicate, paginationPredicate));
        query.orderBy(cb.asc(region.get("name")), cb.asc(country.get("name")));

        TypedQuery<Region> emQuery = em.createQuery(query);
        List<Region> content = setParams(emQuery, nameStartWith, lastSeenName, lastSeenCountryName)
                .setHint("javax.persistence.fetchgraph", regionEntityGraph)
                .setMaxResults(size + 1)
                .getResultList();

        boolean hasNext = content.size() > size;

        Sort sort = Sort.by(Sort.Order.asc("name"), Sort.Order.asc("country.name"));

        return new SliceImpl<>(
                hasNext ? content.subList(0, size) : content,
                PageRequest.of(0, size, sort),
                hasNext);
    }

    private void validateParameters(String rootLastSeenName, String joinLastSeenName, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }

        boolean isFirstPage = (rootLastSeenName == null && joinLastSeenName == null);
        boolean isValidNextPage = (StringUtils.hasText(rootLastSeenName) && StringUtils.hasText(joinLastSeenName));

        if (!isFirstPage && !isValidNextPage) {
            String errorMsg = String.format("Both parameters must be either null (first page) or non-null and not blank (next page). " +
                    "Received: rootLastSeenName=%s, joinLastSeenName=%s", rootLastSeenName, joinLastSeenName);
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        log.debug("Fetching {}: rootLastSeenName={}, joinLastSeenName={}, size={}",
                isFirstPage ? "first page" : "next page", rootLastSeenName, joinLastSeenName, size);
    }


    private Predicate buildNamePrefixPredicate(CriteriaBuilder cb, Root<? extends NameableGeo> root, String nameStartWith) {
        if (nameStartWith == null) {
            return cb.conjunction();
        }

        ParameterExpression<String> namePrefixParam = cb.parameter(String.class, "rootNameStartWith");
        return cb.like(cb.lower(root.get("name")), cb.lower(cb.concat(namePrefixParam, cb.literal("%"))), ESCAPE_CHAR);
    }

    private Predicate buildPaginationPredicate(CriteriaBuilder cb, Root<? extends NameableGeo> root, Join<? extends NameableGeo, ? extends NameableGeo> join,
                                               String lastSeenName, String lastSeenCountryName) {
        if (lastSeenName == null) {
            return cb.conjunction();
        }

        ParameterExpression<String> rootLastSeenNameParam = cb.parameter(String.class, "rootLastSeenName");
        ParameterExpression<String> joinLastSeenNameParam = cb.parameter(String.class, "joinLastSeenName");

        Predicate nameGt = cb.greaterThan(root.get("name"), rootLastSeenNameParam);
        Predicate nameEq = cb.equal(root.get("name"), rootLastSeenNameParam);
        Predicate countryNameGt = cb.greaterThan(join.get("name"), joinLastSeenNameParam);
        return cb.or(nameGt, cb.and(nameEq, countryNameGt));
    }

    private <T> TypedQuery<T> setParams(TypedQuery<T> query, String rootNameStartWith, String rootLastSeenName, String joinLastSeenName) {
        if (rootNameStartWith != null) {
            query.setParameter("rootNameStartWith", escapeLikePattern(rootNameStartWith));
        }
        if (rootLastSeenName != null) {
            query.setParameter("rootLastSeenName", rootLastSeenName);
        }
        if (joinLastSeenName != null) {
            query.setParameter("joinLastSeenName", joinLastSeenName);
        }
        return query;
    }

    /**
     * Escapes special LIKE pattern characters (!, %, _) by prefixing them with the escape character.
     *
     * @param input the string to escape (null returns null)
     * @return escaped string, or original if no special chars found
     * @see #ESCAPE_CHAR
     */
    private String escapeLikePattern(String input) {
        if (input == null || !input.matches(".*[!%_].*")) {
            return input;
        }

        StringBuilder sb = new StringBuilder(input.length() * 2);
        for (char c : input.toCharArray()) {
            if (c == ESCAPE_CHAR || c == '%' || c == '_') {
                sb.append(ESCAPE_CHAR);
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
