package ru.pavbatol.myplace.stats;

import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class StatsService {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(StatsService.class, args);

        TestingMongo testingMongo = new TestingMongo();
        TestingReactMongo testingReactMongo = new TestingReactMongo();

//        testingMongo.setBase();
//        testingReactMongo.setReactiveBase();
//        testingReactMongo.simpleFluxExample();
//        testingReactMongo.backpressureExample();

        int input = 0;
        Scanner scanner = new Scanner(System.in);
        while (input != -1) {
            if (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    input = scanner.nextInt();
                    System.out.println("input = " + input);
                    switch (input) {
                        case -10:
                            System.out.println("Closing app...");
                            int exitCode = SpringApplication.exit(context, () -> 0);
                            System.exit(exitCode);
                            break;
                        case 1:
                            testingMongo.setBase();
                            break;
                        case 2:
                            testingReactMongo.setReactiveBase();
                            break;
                        case 3:
                            testingReactMongo.simpleFluxExample();
                            break;
                        case 4:
                            testingReactMongo.backpressureExample();
                            break;
                        case 5:
                            try {
                                testingReactMongo.coldPublisherExample();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case 6:
                            try {
                                testingReactMongo.hotPublisherExample();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        default:
                            System.out.println("!Command not supported: " + input);
                    }
                } else {
                    scanner.next();
                }
            }
        }
    }
}
