package ru.pavbatol.myplace.geo.management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.pavbatol.myplace.geo.common.IdableNameableGeo;
import ru.pavbatol.myplace.geo.common.IdentifiableGeo;
import ru.pavbatol.myplace.geo.common.NameableGeo;
import ru.pavbatol.myplace.geo.city.model.City;
import ru.pavbatol.myplace.geo.city.repository.CityRepository;
import ru.pavbatol.myplace.geo.country.model.Country;
import ru.pavbatol.myplace.geo.country.repository.CountryRepository;
import ru.pavbatol.myplace.geo.district.model.District;
import ru.pavbatol.myplace.geo.district.repository.DistrictRepository;
import ru.pavbatol.myplace.geo.house.model.House;
import ru.pavbatol.myplace.geo.house.repository.HouseRepository;
import ru.pavbatol.myplace.geo.region.model.Region;
import ru.pavbatol.myplace.geo.region.repository.RegionRepository;
import ru.pavbatol.myplace.geo.street.model.Street;
import ru.pavbatol.myplace.geo.street.repository.StreetRepository;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataImportServiceImpl implements DataImportService {
    private static final String KEY_DELIMITER = "_";
    private static final String CSV_DELIMITER = ",";
    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;
    private final CityRepository cityRepository;
    private final StreetRepository streetRepository;
    private final HouseRepository houseRepository;

    @Override
    @Transactional
    public void importDataFromCsv(OutputStream outputStream, MultipartFile file, boolean responseExportWithId) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<Country> countries = new ArrayList<>();
            List<Region> regions = new ArrayList<>();
            List<District> districts = new ArrayList<>();
            List<City> cities = new ArrayList<>();
            List<Street> streets = new ArrayList<>();
            List<House> houses = new ArrayList<>();

            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                parseLine(line, countries, regions, districts, cities, streets, houses);
            }

            Map<String, Country> countriesToSave = getCountriesToSave(countries);
            Map<String, Region> regionsToSave = getRegionsToSave(regions, countriesToSave);
            Map<String, District> districtsToSave = getDistrictsToSave(districts, regionsToSave);
            Map<String, City> citiesToSave = getCitiesToSave(cities, districtsToSave);
            Map<String, Street> streetsToSave = getStreetsToSave(streets, citiesToSave);
            Map<String, House> housesToSave = getHousesToSave(houses, streetsToSave);

            List<Country> savedCountries = countryRepository.saveAll(countriesToSave.values());
            List<Region> savedRegions = regionRepository.saveAll(regionsToSave.values());
            List<District> savedDistricts = districtRepository.saveAll(districtsToSave.values());
            List<City> savedCities = cityRepository.saveAll(citiesToSave.values());
            List<Street> savedStreets = streetRepository.saveAll(streetsToSave.values());
            List<House> savedHouses = houseRepository.saveAll(housesToSave.values());

            exportSavedDataToCsv(outputStream, savedCountries, savedRegions, savedDistricts, savedCities, savedStreets,
                    savedHouses, responseExportWithId);
        } catch (IOException e) {
            log.error("Error reading file: {}. Message: {}", file.getOriginalFilename(), e.getMessage());
            throw new RuntimeException("Error reading file: " + file.getOriginalFilename(), e);
        }
    }

    private void exportSavedDataToCsv(OutputStream outputStream,
                                      List<Country> countries,
                                      List<Region> regions,
                                      List<District> districts,
                                      List<City> cities,
                                      List<Street> streets,
                                      List<House> houses,
                                      boolean exportWithId) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            writer.write(String.join(", ",
                    "CountryCode", "Country", "Region", "District", "City", "Street", "House", "Latitude", "Longitude"));
            writer.newLine();

            List<IdableNameableGeo> allGeos = new ArrayList<>(houses);
            allGeos.addAll(streets);
            allGeos.addAll(cities);
            allGeos.addAll(districts);
            allGeos.addAll(regions);
            allGeos.addAll(countries);

            allGeos.sort(new GeoComparator());

            writeData(allGeos, writer, exportWithId);

        } catch (IOException e) {
            log.error("Error writing to CSV using OutputStream: {}", e.getMessage());
            throw new RuntimeException("Error writing data to the stream", e);
        }
    }

    private <T extends IdableNameableGeo> void writeData(List<T> geos, BufferedWriter writer, boolean exportWithId) throws IOException {
        Set<Long> processedCountryIds = new HashSet<>();
        Set<Long> processedRegionIds = new HashSet<>();
        Set<Long> processedDistrictIds = new HashSet<>();
        Set<Long> processedCityIds = new HashSet<>();
        Set<Long> processedStreetIds = new HashSet<>();

        StringBuilder line = new StringBuilder();

        for (IdableNameableGeo geo : geos) {
            if (Objects.isNull(geo)) {
                continue;
            }

            line.setLength(0);

            Long geoId = geo.getId();
            if (Objects.nonNull(geoId)) {
                if (geo instanceof Street && !processedStreetIds.add(geoId)
                        || geo instanceof City && !processedCityIds.add(geoId)
                        || geo instanceof District && !processedDistrictIds.add(geoId)
                        || geo instanceof Region && !processedRegionIds.add(geoId)
                        || geo instanceof Country && !processedCountryIds.add(geoId)) {
                    continue;
                }
            }

            House house = (geo instanceof House) ? (House) geo : null;

            Street street = Objects.nonNull(house) ? house.getStreet() : null;
            if (Objects.isNull(street)) {
                street = (geo instanceof Street) ? (Street) geo : null;
            }

            City city = Objects.nonNull(street) ? street.getCity() : null;
            if (Objects.isNull(city)) {
                city = (geo instanceof City) ? (City) geo : null;
            }


            District district = Objects.nonNull(city) ? city.getDistrict() : null;
            if (Objects.isNull(district)) {
                district = (geo instanceof District) ? (District) geo : null;
            }


            Region region = Objects.nonNull(district) ? district.getRegion() : null;
            if (Objects.isNull(region)) {
                region = (geo instanceof Region) ? (Region) geo : null;
            }

            Country country = Objects.nonNull(region) ? region.getCountry() : null;
            if (Objects.isNull(country)) {
                country = (geo instanceof Country) ? (Country) geo : null;
            }

            if (Objects.nonNull(country)) {
                line.append(country.getCode()).append(CSV_DELIMITER);
            }
            appendGeoDetailsAndCollectIds(country, line, processedCountryIds, exportWithId).append(CSV_DELIMITER);
            appendGeoDetailsAndCollectIds(region, line, processedRegionIds, exportWithId).append(CSV_DELIMITER);
            appendGeoDetailsAndCollectIds(district, line, processedDistrictIds, exportWithId).append(CSV_DELIMITER);
            appendGeoDetailsAndCollectIds(city, line, processedCityIds, exportWithId).append(CSV_DELIMITER);
            appendGeoDetailsAndCollectIds(street, line, processedStreetIds, exportWithId).append(CSV_DELIMITER);
            appendGeoDetails(house, line, exportWithId);
            if (Objects.nonNull(house)) {
                line.append(CSV_DELIMITER).append(house.getLat()).append(CSV_DELIMITER).append(house.getLon());
            }

            writer.write(line.toString());
            writer.newLine();
        }
    }

    private StringBuilder processGeoDetailsByAppendingAndCollecting(@Nullable IdableNameableGeo geoEntity,
                                                                    @NotNull StringBuilder stringBuilder,
                                                                    @Nullable Set<Long> ids,
                                                                    boolean exportWithId) {
        Optional<IdableNameableGeo> optGoEntity = Optional.ofNullable(geoEntity);
        Optional<Long> optId = optGoEntity.map(IdentifiableGeo::getId);

        if (Objects.nonNull(ids)) {
            optId.ifPresent(ids::add);
        }

        String name = optGoEntity.map(NameableGeo::getName).orElse("");

        return exportWithId
                ? stringBuilder.append("[")
                .append(optId.map(id -> Long.toString(id)).orElse(" "))
                .append("]")
                .append(" ")
                .append(name)
                : stringBuilder.append(name);
    }

    private StringBuilder appendGeoDetailsAndCollectIds(IdableNameableGeo geoEntity, StringBuilder stringBuilder,
                                                        @NotNull Set<Long> ids, boolean exportWithId) {
        return processGeoDetailsByAppendingAndCollecting(geoEntity, stringBuilder, ids, exportWithId);
    }

    private StringBuilder appendGeoDetails(IdableNameableGeo geoEntity, StringBuilder stringBuilder, boolean exportWithId) {
        return processGeoDetailsByAppendingAndCollecting(geoEntity, stringBuilder, null, exportWithId);
    }

    private void parseLine(String line, List<Country> countries, List<Region> regions,
                           List<District> districts, List<City> cities, List<Street> streets,
                           List<House> houses) {
        int minFieldsRequired = 9;
        String[] fields = line.split(CSV_DELIMITER, -1);
        if (fields.length < minFieldsRequired) {
            log.error("Invalid CSV string format. Expected at least {} fields but got: [{}]. Line: {}", minFieldsRequired, fields.length, line);
            throw new IllegalArgumentException("Invalid CSV file format. Line: " + line);
        }

        String countryName = fields[0].trim();
        String countryCode = fields[1].trim();
        String regionName = fields[2].trim();
        String districtName = fields[3].trim();
        String cityName = fields[4].trim();
        String streetName = fields[5].trim();
        String houseNumber = fields[6].trim();
        double houseLat = parseCoordinate(fields[7]);
        double houseLon = parseCoordinate(fields[8]);

        Country country = null;
        Region region = null;
        District district = null;
        City city = null;
        Street street = null;
        House house;

        if (!countryName.isEmpty()) {
            country = new Country().setName(countryName).setCode(countryCode.toUpperCase());
            countries.add(country);
        }
        if (!regionName.isEmpty() && Objects.nonNull(country)) {
            region = new Region().setName(regionName).setCountry(country);
            regions.add(region);
        }
        if (!districtName.isEmpty() && Objects.nonNull(region)) {
            district = new District().setName(districtName).setRegion(region);
            districts.add(district);
        }
        if (!cityName.isEmpty() && Objects.nonNull(district)) {
            city = new City().setName(cityName).setDistrict(district);
            cities.add(city);
        }
        if (!streetName.isEmpty() && Objects.nonNull(city)) {
            street = new Street().setName(streetName).setCity(city);
            streets.add(street);
        }
        if (!houseNumber.isEmpty() && Objects.nonNull(street)) {
            house = new House().setNumber(houseNumber).setStreet(street).setLat(houseLat).setLon(houseLon);
            houses.add(house);
        }
    }

    private int calculateInitialCapacity(int size) {
        return Math.max(16, (int) Math.ceil(size / 0.75));
    }

    private double parseCoordinate(String coordField) {
        if (coordField.trim().isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(coordField.trim());
        } catch (NumberFormatException e) {
            log.error("Error converting coordinates: '{}'. Error: {}", coordField, e.getMessage());
            throw new IllegalArgumentException("Invalid coordinate format: " + coordField, e);
        }
    }

    private Optional<String> createFormattedCountryKey(Country country) {
        return Optional.ofNullable(country)
                .map(Country::getName)
                .map(String::toLowerCase);
    }

    private Map<String, Country> getCountriesToSave(List<Country> countries) {
        List<Country> existingCountries = countryRepository.findByNameInIgnoreCase(countries.stream()
                .filter(Objects::nonNull)
                .map(Country::getName)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList()));

        Map<String, Country> countriesToSave = new HashMap<>(calculateInitialCapacity(countries.size()));

        existingCountries.forEach(country -> createFormattedCountryKey(country).ifPresent(formattedKey ->
                countriesToSave.put(formattedKey, country)));
        countries.forEach(country -> createFormattedCountryKey(country).ifPresent(formattedKey ->
                countriesToSave.putIfAbsent(formattedKey, country)));

        return countriesToSave;
    }

    private void updateRegionsWithCountriesAndCollectInfo(List<Region> regions, Map<String, Country> countriesToSave,
                                                          Set<String> regionNamesLowercase, Set<Long> countryIds) {
        regions.forEach(region -> Optional.ofNullable(region)
                .filter(r -> Objects.nonNull(r.getName()))
                .map(Region::getCountry)
                .flatMap(this::createFormattedCountryKey)
                .ifPresent(formattedKey -> {
                    Country newCountry = countriesToSave.get(formattedKey);
                    region.setCountry(newCountry);

                    Optional.ofNullable(newCountry)
                            .map(Country::getId)
                            .ifPresent(countryId -> {
                                regionNamesLowercase.add(region.getName().toLowerCase());
                                countryIds.add(countryId);
                            });
                }));
    }

    private Optional<String> createFormattedRegionKey(Region region) {
        return Optional.ofNullable(region)
                .map(Region::getName)
                .map(String::toLowerCase)
                .flatMap(regionName -> Optional.ofNullable(region.getCountry())
                        .flatMap(this::createFormattedCountryKey)
                        .map(formattedCountryKey -> String.join(KEY_DELIMITER, regionName, formattedCountryKey)));
    }

    private Map<String, Region> getRegionsToSave(List<Region> regions, Map<String, Country> countriesToSave) {
        Set<String> regionNamesLowercase = new HashSet<>();
        Set<Long> countryIds = new HashSet<>();

        updateRegionsWithCountriesAndCollectInfo(regions, countriesToSave, regionNamesLowercase, countryIds);

        List<Region> existingRegions = regionRepository.findByNameInIgnoreCaseAndCountryIdIn(
                regionNamesLowercase, countryIds);

        Map<String, Region> regionsToSave = new HashMap<>(calculateInitialCapacity(regions.size()));

        existingRegions.forEach(region -> createFormattedRegionKey(region).ifPresent(formattedKey ->
                regionsToSave.put(formattedKey, region)));
        regions.forEach(region -> createFormattedRegionKey(region).ifPresent(formattedKey ->
                regionsToSave.putIfAbsent(formattedKey, region)));

        return regionsToSave;
    }

    private void updateDistrictsWithRegionsAndCollectInfo(List<District> districts, Map<String, Region> regionsToSave,
                                                          Set<String> districtNamesLowercase, Set<Long> regionIds) {
        districts.forEach(district -> Optional.ofNullable(district)
                .filter(d -> Objects.nonNull(d.getName()))
                .map(District::getRegion)
                .flatMap(this::createFormattedRegionKey)
                .ifPresent(formattedKey -> {
                    Region newRegion = regionsToSave.get(formattedKey);
                    district.setRegion(newRegion);

                    Optional.ofNullable(newRegion)
                            .map(Region::getId)
                            .ifPresent(regionId -> {
                                districtNamesLowercase.add(district.getName().toLowerCase());
                                regionIds.add(regionId);
                            });
                }));
    }

    private Optional<String> createFormattedDistrictKey(District district) {
        return Optional.ofNullable(district)
                .map(District::getName)
                .map(String::toLowerCase)
                .flatMap(districtName -> Optional.ofNullable(district.getRegion())
                        .flatMap(this::createFormattedRegionKey)
                        .map(formattedRegionKey -> String.join(KEY_DELIMITER, districtName, formattedRegionKey)));
    }

    private Map<String, District> getDistrictsToSave(List<District> districts, Map<String, Region> regionsToSave) {
        Set<String> districtNamesLowercase = new HashSet<>();
        Set<Long> regionIds = new HashSet<>();

        updateDistrictsWithRegionsAndCollectInfo(districts, regionsToSave, districtNamesLowercase, regionIds);

        List<District> existingDistricts = districtRepository.findByNameInIgnoreCaseAndRegionIdIn(
                districtNamesLowercase, regionIds);

        Map<String, District> districtsToSave = new HashMap<>(calculateInitialCapacity(districts.size()));

        existingDistricts.forEach(district -> createFormattedDistrictKey(district).ifPresent(formattedKey ->
                districtsToSave.put(formattedKey, district)));
        districts.forEach(district -> createFormattedDistrictKey(district).ifPresent(formattedKey ->
                districtsToSave.putIfAbsent(formattedKey, district)));

        return districtsToSave;
    }

    private void updateCitiesWithDistrictsAndCollectInfo(List<City> cities, Map<String, District> districtsToSave,
                                                         Set<String> cityNamesLowercase, Set<Long> districtIds) {
        cities.forEach(city -> Optional.ofNullable(city)
                .filter(c -> Objects.nonNull(c.getName()))
                .map(City::getDistrict)
                .flatMap(this::createFormattedDistrictKey)
                .ifPresent(formattedKey -> {
                    District newDistrict = districtsToSave.get(formattedKey);
                    city.setDistrict(newDistrict);

                    if (Objects.nonNull(newDistrict)) {
                        Long districtId = newDistrict.getId();
                        if (Objects.nonNull(districtId)) {
                            cityNamesLowercase.add(city.getName().toLowerCase());
                            districtIds.add(districtId);
                        }
                    }
                }));
    }

    private Optional<String> createFormattedCityKey(City city) {
        return Optional.ofNullable(city)
                .map(City::getName)
                .map(String::toLowerCase)
                .flatMap(cityName -> Optional.ofNullable(city.getDistrict())
                        .flatMap(this::createFormattedDistrictKey)
                        .map(formattedDistrictKey -> String.join(KEY_DELIMITER, cityName, formattedDistrictKey)));
    }

    private Map<String, City> getCitiesToSave(List<City> cities, Map<String, District> districtsToSave) {
        Set<String> cityNamesLowercase = new HashSet<>();
        Set<Long> districtIds = new HashSet<>();

        updateCitiesWithDistrictsAndCollectInfo(cities, districtsToSave, cityNamesLowercase, districtIds);

        List<City> existingCities = cityRepository.findByNameInIgnoreCaseAndDistrictIdIn(cityNamesLowercase, districtIds);

        Map<String, City> citiesToSave = new HashMap<>(calculateInitialCapacity(cities.size()));

        existingCities.forEach(city -> createFormattedCityKey(city).ifPresent(formattedKey ->
                citiesToSave.put(formattedKey, city)));
        cities.forEach(city -> createFormattedCityKey(city).ifPresent(formattedKey ->
                citiesToSave.putIfAbsent(formattedKey, city)));

        return citiesToSave;
    }

    private void updateStreetsWithCitiesAndCollectInfo(List<Street> streets, Map<String, City> citiesToSave,
                                                       Set<String> streetNamesLowercase, Set<Long> cityIds) {
        streets.forEach(street -> Optional.ofNullable(street)
                .filter(s -> Objects.nonNull(s.getName()))
                .map(Street::getCity)
                .flatMap(this::createFormattedCityKey)
                .ifPresent(formattedKey -> {
                    City newCity = citiesToSave.get(formattedKey);
                    street.setCity(newCity);

                    Optional.ofNullable(newCity)
                            .map(City::getId)
                            .ifPresent(citiId -> {
                                streetNamesLowercase.add(street.getName().toLowerCase());
                                cityIds.add(citiId);
                            });
                }));
    }

    private Optional<String> createFormattedStreetKey(Street street) {
        return Optional.ofNullable(street)
                .map(Street::getName)
                .map(String::toLowerCase)
                .flatMap(streetName -> Optional.ofNullable(street.getCity())
                        .flatMap(this::createFormattedCityKey)
                        .map(formattedCityKey -> String.join(KEY_DELIMITER, streetName, formattedCityKey)));
    }

    private Map<String, Street> getStreetsToSave(List<Street> streets, Map<String, City> citiesToSave) {
        Set<String> streetNamesLowercase = new HashSet<>();
        Set<Long> cityIds = new HashSet<>();

        updateStreetsWithCitiesAndCollectInfo(streets, citiesToSave, streetNamesLowercase, cityIds);

        List<Street> existingStreets = streetRepository.findByNameInIgnoreCaseAndCityIdIn(streetNamesLowercase, cityIds);

        Map<String, Street> streetsToSave = new HashMap<>(calculateInitialCapacity(streets.size()));

        existingStreets.forEach(street -> createFormattedStreetKey(street).ifPresent(formattedKey ->
                streetsToSave.put(formattedKey, street)));
        streets.forEach(street -> createFormattedStreetKey(street).ifPresent(formattedKey ->
                streetsToSave.putIfAbsent(formattedKey, street)));

        return streetsToSave;
    }

    private void updateHousesWithStreetsAndCollectInfo(List<House> houses, Map<String, Street> streetsToSave,
                                                       Set<String> houseNumberLowercase, Set<Long> streetIds) {
        houses.forEach(house -> Optional.ofNullable(house)
                .filter(h -> Objects.nonNull(h.getNumber()))
                .map(House::getStreet)
                .flatMap(this::createFormattedStreetKey)
                .ifPresent(formattedKey -> {
                    Street newStreet = streetsToSave.get(formattedKey);
                    house.setStreet(newStreet);

                    Optional.ofNullable(newStreet)
                            .map(Street::getId)
                            .ifPresent(streetId -> {
                                houseNumberLowercase.add(house.getNumber().toLowerCase());
                                streetIds.add(streetId);
                            });
                }));
    }

    private Optional<String> createFormattedHoseKey(House house) {
        return Optional.ofNullable(house)
                .map(House::getNumber)
                .map(String::toLowerCase)
                .flatMap(houseNumber -> Optional.ofNullable(house.getStreet())
                        .flatMap(this::createFormattedStreetKey)
                        .map(formattedStreetKey -> String.join(KEY_DELIMITER, houseNumber, formattedStreetKey)));
    }

    private Map<String, House> getHousesToSave(List<House> houses, Map<String, Street> streetsToSave) {
        Set<String> houseNumbersLowercase = new HashSet<>();
        Set<Long> streetIds = new HashSet<>();

        updateHousesWithStreetsAndCollectInfo(houses, streetsToSave, houseNumbersLowercase, streetIds);

        List<House> existingHouses = houseRepository.findByNumberInIgnoreCaseAndStreetIdIn(houseNumbersLowercase, streetIds);

        Map<String, House> housesToSave = new HashMap<>(calculateInitialCapacity(houses.size()));

        existingHouses.forEach(house -> createFormattedHoseKey(house).ifPresent(formattedKey ->
                housesToSave.put(formattedKey, house)));
        houses.forEach(house -> createFormattedHoseKey(house).ifPresent(formattedKey ->
                housesToSave.putIfAbsent(formattedKey, house)));

        return housesToSave;
    }
}
