package ru.pavbatol.myplace.geo.country.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
public interface CountryRepository extends JpaRepository<Country, Long>, CustomCountryRepository {

    /**
     * Finds countries whose names match any of the provided values (case-insensitive).
     *
     * @param names list of country names to search for (case doesn't matter)
     * @return list of matching countries, empty list if no matches found
     * @throws IllegalArgumentException if the input list is null
     */
    List<Country> findByNameInIgnoreCase(List<String> names);
}
