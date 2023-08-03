package ru.pavbatol.myplace.stats.shipping.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.shipping.ShippingGeoDtoResponse;
import ru.pavbatol.myplace.stats.shipping.model.ShippingGeo;

import java.time.LocalDateTime;

@Repository
public interface ShippingGeoMongoRepository extends ReactiveMongoRepository<ShippingGeo, String>, CustomShippingGeoMongoRepository {
//    @Aggregation(pipeline = {
//            "{$group: {_id: '$country', cities: {$addToSet: '$city'}}}",
//            "{$project: {country: '$_id', cities: 1}}",
//            "{$group: {_id: null, countryCities: {$push: {k: '$country', v: '$cities'}}}}",
//            "{$addFields: {countryCount: {$size: '$countryCities'}}, cityCount: {$sum: {$map: {input: '$countryCities', as: 'cc', in: {$size: '$$cc.v'}}}}}}",
//            "{$project: {_id: 0, countryCities: 1, countryCount: 1, cityCount: 1}}"
//    })

//    @Aggregation(pipeline = {
//            "{$match: {timestamp: {$gte: ?0, $lte: ?1}}}",
//            "{$match: {'itemId': {$in: ?2}}}",
//            "{$match: {'country': {$in: ?3}}}",
//            "{$group: {" +
//                    "_id: { itemId: '$itemId' }, " +
//                    "cities: { $push: '$city' },  " +
//                    "countries: { $push: '$country' }  }}",
////                    "cities: { $addToSet: '$city' },  " +
////                    "countries: { $addToSet: '$country' }  }}",
//            "{$project: {" +
//                    "_id: 0, " +
//                    "itemId: '$_id.itemId', " +
//                    "countryCount: { $size: '$countries' }, " +
//                    "cityCount: { $size: '$cities' }, " +
//                    "countries: 1, " +
//                    "cities: 1 } }",
//            "{$sort: { itemId: 1 } }"
//    })

//    @Aggregation(pipeline = {
//        "  {$match: {timestamp: {$gte: ?0, $lte: ?1}}}",
////        "  { $group: { _id: { itemId: '$itemId', country: '$country' }, cities: { $push: '$city' } } }",
//        "  { $group: { _id: { itemId: '$itemId', country: '$country' }, cities: { $addToSet: '$city' } } }",
//        "  { $group: { _id: '$_id.itemId', countries: { $push: { country: '$_id.country', cities: '$cities', cCount: {$size: '$cities'} } } } }",
//        "  { $project: { _id: 0, itemId: '$_id', countryCount: { $size: '$countries' }, cityCount: { $sum: '$countries.cCount' }," +
//        "      countryCities: { $arrayToObject: { $map: { input: '$countries', as: 'country', in: { k: '$$country.country', v: '$$country.cities' } } } } " +
//        "    } }",
//        "  { $sort: { cityCount: ?2, itemId: 1 } }"
//    })

    @Aggregation(pipeline = {
            "  {$match: {timestamp: {$gte: ?0, $lte: ?1}}}",
            "  {$group: { " +
                    "      _id: { itemId: '$itemId', country: '$country' }, " +
//                    "      cities: { $push: '$city' } " +
                    "      cities: { $addToSet: '$city' } " +
                    "    } " +
                    "  }",
            "  {$group: { " +
                    "      _id: '$_id.itemId', " +
                    "      countries: { $push: { country: '$_id.country', cities: '$cities', cCount: {$size: '$cities'} } } " +
                    "    } " +
                    "  }",
            "  {$project: { " +
                    "      _id: 0, " +
                    "      itemId: '$_id', " +
                    "      countryCount: { $size: '$countries' }, " +
                    "      cityCount: { $sum: '$countries.cCount' }, " +
                    "      countryCities: { " +
                    "        $arrayToObject: { " +
                    "          $map: { " +
                    "            input: '$countries', " +
                    "            as: 'country', " +
                    "            in: { " +
                    "              k: '$$country.country', " +
                    "              v: '$$country.cities' " +
                    "            } " +
                    "          } " +
                    "        } " +
                    "      } " +
                    "    } " +
                    "  }",
            "  {$sort: { cityCount: ?2, itemId: 1 }}"
    })
    Flux<ShippingGeoDtoResponse> findShippingCountryCitiesByDateAndUnique(LocalDateTime start, LocalDateTime end, int sort);
}
