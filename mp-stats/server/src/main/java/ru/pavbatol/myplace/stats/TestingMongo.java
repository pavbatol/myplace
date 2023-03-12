package ru.pavbatol.myplace.stats;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class TestingMongo {

    public void setBase() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("myMongoDb");

            boolean collectionExists = database.listCollectionNames().into(new ArrayList<>()).contains("testCollection");
            if (!collectionExists) {
                log.info("Collection NOT exists");
                database.createCollection("testCollection");
            } else {
                log.info("Collection exists");
            }

            mongoClient.listDatabaseNames().forEach(System.out::println);
            log.info("MongoClients.create(\"mongodb://localhost:27017\")");

        } catch (RuntimeException e) {
            log.error("Error for MongoClients.create(\"mongodb://localhost:27017\")");
        }
    }

}
