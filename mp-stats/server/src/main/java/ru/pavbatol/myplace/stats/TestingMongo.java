package ru.pavbatol.myplace.stats;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

//import com.mongodb.reactivestreams.client.MongoClient;
//import com.mongodb.reactivestreams.client.MongoClients;
//import com.mongodb.reactivestreams.client.MongoCollection;
//import com.mongodb.reactivestreams.client.MongoDatabase;
import java.util.concurrent.CountDownLatch;

import com.mongodb.client.result.InsertOneResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;

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

    public void setReactiveBase() {
        try (com.mongodb.reactivestreams.client.MongoClient mongoClient
                     = com.mongodb.reactivestreams.client.MongoClients.create("mongodb://localhost:27017")) {

            log.debug("Trying get DB");
            com.mongodb.reactivestreams.client.MongoDatabase database = mongoClient.getDatabase("myReactiveDb");
            log.debug("Got DB");

            log.debug("Prepare Document");
            Document doc = new Document("name", "MongoDB")
                    .append("type", "database")
                    .append("count", 1)
                    .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                    .append("info", new Document("x", 203).append("y", 102));



//            CountDownLatch latch = new CountDownLatch(1);
//            database.listCollections().subscribe(new Subscriber<Document>() {
//                @Override
//                public void onSubscribe(Subscription subscription) {
//                    System.out.println("subscription");
//                    subscription.request(Long.MAX_VALUE); // Request all the collections.
//                }
//
//                @Override
//                public void onNext(Document document) {
//                    System.out.println("next: " + document);
//                }
//
//                @Override
//                public void onError(Throwable throwable) {
//                    System.out.println("error");
//                    throwable.printStackTrace();
//                    latch.countDown();
//                }
//
//                @Override
//                public void onComplete() {
//                    System.out.println("await");
//                    try {
//                        latch.await();
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });

            log.debug("getCollection");
            com.mongodb.reactivestreams.client.MongoCollection<Document> collection = database.getCollection("test");

            log.debug("getPublisher");
            Publisher<InsertOneResult> publisher = collection.insertOne(doc);

            log.debug("trying subscribe");
            publisher.subscribe(new Subscriber<>() {
                @Override
                public void onSubscribe(final Subscription s) {
                    s.request(1);  // <--- Data requested and the insertion will now occur
                }

                @Override
                public void onNext(final InsertOneResult result) {
                    System.out.println("Inserted: " + result);
                }

                @Override
                public void onError(final Throwable t) {
                    System.out.println("Failed");
                }

                @Override
                public void onComplete() {
                    System.out.println("Completed");
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
