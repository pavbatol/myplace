package ru.pavbatol.myplace.geo.country.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pavbatol.myplace.geo.country.model.Country;

import java.util.List;

/**
 * JPA repository for {@link Country} entities.
 * Provides methods for country data access including paginated queries and case-insensitive searches.
 *
 * @see Country
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    /**
     * Finds next page of countries alphabetically after last seen country name.
     * Results are always ordered by name in ascending direction (A-Z).
     *
     * @param lastSeenName last country name from previous page (excluded from results), null for first page
     * @param pageable     pagination information (page size will be respected,
     *                     page number should be 0 for cursor pagination)
     */
    @Query("SELECT c FROM Country c " +
            "WHERE :lastSeenName IS NULL OR c.name > :lastSeenName " +
            "ORDER BY c.name ASC")
    Slice<Country> findNextPage(@Param("lastSeenName") String lastSeenName, Pageable pageable);

    /**
     * Finds countries starting with prefix (case-insensitive) and after last seen name.
     * Results are always ordered by name in ascending direction (A-Z).
     *
     * @param nameStartWith prefix to search (e.g. "Rus" for Russia)
     * @param lastSeenName  last country name from previous page (exclusive), null for first page
     * @param pageable      pagination information (page size will be respected,
     *                      page number should be 0 for cursor pagination)
     */
    @Query("SELECT c FROM Country c " +
            "WHERE LOWER(c.name) LIKE LOWER(CONCAT(:nameStartWith, '%')) " +
            "AND (:lastSeenName IS NULL OR c.name > :lastSeenName) " +
            "ORDER BY c.name ASC")
    Slice<Country> findNextPageByNamePrefixIgnoreCase(@Param("nameStartWith") String nameStartWith,
                                                      @Param("lastSeenName") String lastSeenName,
                                                      Pageable pageable);

    /**
     * Default implementation for cursor pagination - always uses first page (offset 0)
     * and explicit name ascending sort to ensure consistent ordering.
     *
     * <p>The returned Slice will have {@code slice.getSort().isSorted() == true}
     * with {@code name ASC} ordering to match the actual query results.
     *
     * @param lastSeenName last country name from previous page (exclusive), null for first page
     * @param size         page size
     * @return Slice with paginated results and sort metadata
     * @see #findNextPage(String, Pageable)
     */
    default Slice<Country> findNextPage(String lastSeenName, int size) {
        return findNextPage(lastSeenName, PageRequest.of(0, size, Sort.by("name").ascending()));
    }

    /**
     * Default implementation for prefix search - always uses first page (offset 0)
     * and explicit name ascending sort to ensure consistent ordering.
     *
     * <p>The returned Slice will have {@code slice.getSort().isSorted() == true}
     * with {@code name ASC} ordering to match the actual query results.
     *
     * @param nameStartWith prefix to search (case-insensitive)
     * @param lastSeenName  last country name from previous page (exclusive), null for first page
     * @param size          page size
     * @return Slice with paginated results and sort metadata
     * @see #findNextPageByNamePrefixIgnoreCase(String, String, Pageable)
     */
    default Slice<Country> findNextPageByNamePrefixIgnoreCase(String nameStartWith, String lastSeenName, int size) {
        return findNextPageByNamePrefixIgnoreCase(nameStartWith, lastSeenName, PageRequest.of(0, size, Sort.by("name").ascending()));
    }

    /**
     * Finds countries whose names match any of the provided values (case-insensitive).
     *
     * @param names list of country names to search for (case doesn't matter)
     * @return list of matching countries, empty list if no matches found
     * @throws IllegalArgumentException if the input list is null
     */
    List<Country> findByNameInIgnoreCase(List<String> names);
}
