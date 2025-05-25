package ru.pavbatol.myplace.geo.region.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.pavbatol.myplace.app.util.SqlUtils;
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
    private static final String ATTR_NAME = "name";
    private static final String ATTR_COUNTRY = "country";
    private static final String ATTR_COUNTRY_NAME = "country.name";
    private static final String PARAM_ROOT_NAME_START_WITH = "rootNameStartWith";
    private static final String PARAM_ROOT_LAST_SEEN_NAME = "rootLastSeenName";
    private static final String PARAM_JOIN_LAST_SEEN_NAME = "joinLastSeenName";
    private static final char ESCAPE_CHAR = '!';
    private final EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public Slice<Region> findPageByNamePrefixIgnoreCase(String nameStartWith, String lastSeenName, String lastSeenCountryName, int size) {
        validateParameters(lastSeenName, lastSeenCountryName, size);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Region> query = cb.createQuery(Region.class);
        Root<Region> region = query.from(Region.class);
        Join<Region, Country> country = region.join(ATTR_COUNTRY, JoinType.LEFT);

        EntityGraph<Region> regionEntityGraph = em.createEntityGraph(Region.class);
        regionEntityGraph.addAttributeNodes(ATTR_COUNTRY);

        Predicate prefixPredicate = buildRootNamePrefixPredicate(cb, region, nameStartWith);
        Predicate paginationPredicate = buildPaginationPredicate(cb, region, country, lastSeenName);

        query.where(cb.and(prefixPredicate, paginationPredicate));
        query.orderBy(cb.asc(region.get(ATTR_NAME)), cb.asc(country.get(ATTR_NAME)));

        TypedQuery<Region> emQuery = em.createQuery(query);
        List<Region> content = setParams(emQuery, nameStartWith, lastSeenName, lastSeenCountryName)
                .setHint("javax.persistence.fetchgraph", regionEntityGraph)
                .setMaxResults(size + 1)
                .getResultList();

        boolean hasNext = content.size() > size;
        Sort sort = Sort.by(Sort.Order.asc(ATTR_NAME), Sort.Order.asc(ATTR_COUNTRY_NAME));

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
            throw new IllegalArgumentException(String.format("Both parameters must be either null (first page) or non-null and not blank (next page). " +
                    "Received: rootLastSeenName=%s, joinLastSeenName=%s", rootLastSeenName, joinLastSeenName));
        }

        log.debug("Fetching {}: rootLastSeenName={}, joinLastSeenName={}, size={}",
                isFirstPage ? "first page" : "next page", rootLastSeenName, joinLastSeenName, size);
    }


    private Predicate buildRootNamePrefixPredicate(CriteriaBuilder cb, Root<? extends NameableGeo> root, String rootNameStartWith) {
        if (rootNameStartWith == null) {
            return cb.conjunction();
        }

        ParameterExpression<String> namePrefixParam = cb.parameter(String.class, PARAM_ROOT_NAME_START_WITH);
        return cb.like(cb.lower(root.get(ATTR_NAME)), cb.lower(cb.concat(namePrefixParam, cb.literal("%"))), ESCAPE_CHAR);
    }

    private Predicate buildPaginationPredicate(CriteriaBuilder cb,
                                               Root<? extends NameableGeo> root,
                                               Join<? extends NameableGeo, ? extends NameableGeo> join,
                                               String rootLastSeenName) {
        if (rootLastSeenName == null) {
            return cb.conjunction();
        }

        ParameterExpression<String> rootLastSeenNameParam = cb.parameter(String.class, PARAM_ROOT_LAST_SEEN_NAME);
        ParameterExpression<String> joinLastSeenNameParam = cb.parameter(String.class, PARAM_JOIN_LAST_SEEN_NAME);

        Predicate nameGt = cb.greaterThan(root.get(ATTR_NAME), rootLastSeenNameParam);
        Predicate nameEq = cb.equal(root.get(ATTR_NAME), rootLastSeenNameParam);
        Predicate countryNameGt = cb.greaterThan(join.get(ATTR_NAME), joinLastSeenNameParam);
        return cb.or(nameGt, cb.and(nameEq, countryNameGt));
    }

    private <T> TypedQuery<T> setParams(TypedQuery<T> query, String rootNameStartWith, String rootLastSeenName, String joinLastSeenName) {
        if (rootNameStartWith != null) {
            query.setParameter(PARAM_ROOT_NAME_START_WITH, SqlUtils.escapeSqlLikeWildcards(rootNameStartWith, ESCAPE_CHAR));
        }
        if (rootLastSeenName != null) {
            query.setParameter(PARAM_ROOT_LAST_SEEN_NAME, rootLastSeenName);
            query.setParameter(PARAM_JOIN_LAST_SEEN_NAME, joinLastSeenName);
        }
        return query;
    }
}
