package ru.pavbatol.myplace;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
public class TestingMongo {

    public void setBase() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("myMongoDb");
            mongoClient.listDatabaseNames().forEach(System.out::println);


        } catch (RuntimeException e) {
            System.out.println("Error for MongoClients.create(\"mongodb://localhost:27017\")");
        }

    }
}
