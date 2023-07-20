package ru.pavbatol.myplace.stats.view.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.pavbatol.myplace.stats.view.model.View;

public interface ViewMongoRepository extends ReactiveMongoRepository<View, String> {

}
