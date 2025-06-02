package ru.pavbatol.myplace.geo.management.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.pavbatol.myplace.geo.common.NameableGeo;
import ru.pavbatol.myplace.geo.city.model.City;
import ru.pavbatol.myplace.geo.country.model.Country;
import ru.pavbatol.myplace.geo.district.model.District;
import ru.pavbatol.myplace.geo.house.model.House;
import ru.pavbatol.myplace.geo.region.model.Region;
import ru.pavbatol.myplace.geo.street.model.Street;

import java.util.Comparator;
import java.util.Optional;

public class GeoComparator implements Comparator<NameableGeo> {

    @Override
    public int compare(NameableGeo o1, NameableGeo o2) {
        ComparisonData data1 = extractComparisonData(o1);
        ComparisonData data2 = extractComparisonData(o2);

        int result = compareByName(data1.getCountry(), data2.getCountry());
        if (result != 0) {
            return result;
        }

        result = compareByName(data1.getRegion(), data2.getRegion());
        if (result != 0) {
            return result;
        }

        result = compareByName(data1.getDistrict(), data2.getDistrict());
        if (result != 0) {
            return result;
        }

        result = compareByName(data1.getCity(), data2.getCity());
        if (result != 0) {
            return result;
        }

        result = compareByName(data1.getStreet(), data2.getStreet());
        if (result != 0) {
            return result;
        }

        return compareByName(data1.getHouse(), data2.getHouse());
    }

    private ComparisonData extractComparisonData(NameableGeo obj) {
        if (obj == null) {
            return new ComparisonData(null, null, null, null, null, null);
        }

        if (obj instanceof House) {
            House house = (House) obj;
            Street street = house.getStreet();
            City city = safeGetCity(street);
            District district = safeGetDistrict(city);
            Region region = safeGetRegion(district);
            Country country = safeGetCountry(region);

            return new ComparisonData(country, region, district, city, street, house);

        } else if (obj instanceof Street) {
            Street street = (Street) obj;
            City city = street.getCity();
            District district = safeGetDistrict(city);
            Region region = safeGetRegion(district);
            Country country = safeGetCountry(region);

            return new ComparisonData(country, region, district, city, street, null);

        } else if (obj instanceof City) {
            City city = (City) obj;
            District district = city.getDistrict();
            Region region = safeGetRegion(district);
            Country country = safeGetCountry(region);

            return new ComparisonData(country, region, district, city, null, null);

        } else if (obj instanceof District) {
            District district = (District) obj;
            Region region = district.getRegion();
            Country country = safeGetCountry(region);

            return new ComparisonData(country, region, district, null, null, null);

        } else if (obj instanceof Region) {
            Region region = (Region) obj;
            Country country = region.getCountry();

            return new ComparisonData(country, region, null, null, null, null);

        } else if (obj instanceof Country) {
            Country country = (Country) obj;
            return new ComparisonData(country, null, null, null, null, null);
        }

        return new ComparisonData(null, null, null, null, null, null);
    }

    private City safeGetCity(Street street) {
        return street != null ? street.getCity() : null;
    }

    private District safeGetDistrict(City city) {
        return city != null ? city.getDistrict() : null;
    }

    private Region safeGetRegion(District district) {
        return district != null ? district.getRegion() : null;
    }

    private Country safeGetCountry(Region region) {
        return region != null ? region.getCountry() : null;
    }

    private int compareByName(NameableGeo o1, NameableGeo o2) {
        Optional<Integer> result = compareNulls(o1, o2);
        if (result.isPresent()) {
            return result.get();
        }

        String name1 = o1.getName();
        String name2 = o2.getName();
        result = compareNulls(name1, name2);

        return result.orElseGet(() -> name1.compareTo(name2));
    }

    private Optional<Integer> compareNulls(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return Optional.of(0);
        } else if (o1 == null) {
            return Optional.of(1);
        } else if (o2 == null) {
            return Optional.of(-1);
        }

        return Optional.empty();
    }

    @Getter
    @RequiredArgsConstructor
    private class ComparisonData {
        private final Country country;
        private final Region region;
        private final District district;
        private final City city;
        private final Street street;
        private final House house;
    }
}
