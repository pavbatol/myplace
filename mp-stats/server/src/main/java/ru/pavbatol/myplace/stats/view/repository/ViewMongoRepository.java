package ru.pavbatol.myplace.stats.view.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pavbatol.myplace.stats.view.model.View;

@Repository
public interface ViewMongoRepository extends ReactiveMongoRepository<View, String>, CustomViewMongoRepository {

}
