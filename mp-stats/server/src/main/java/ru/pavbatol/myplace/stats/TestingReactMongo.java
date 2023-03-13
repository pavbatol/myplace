package ru.pavbatol.myplace.stats;


import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
public class TestingReactMongo {


    public void setReactiveBase() {
        /*
        String uri = "mongodb://<username>:<Password>@<hostname>:27017/?ssl=true&ssl_ca_certs=cert";
        MongoClientSettings settings = MongoClientSettings.builder()
                .streamFactoryFactory(new NettyStreamFactoryFactory())
                .applyConnectionString(new ConnectionString(uri))
                .build();

        com.mongodb.async.client.MongoClient mongoClient = MongoClients.create(settings);
        */

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            log.debug("Trying get DB");
            MongoDatabase database = mongoClient.getDatabase("myReactiveMongoDb");

            log.debug("getCollection");
            MongoCollection<Document> collection = database.getCollection("test");

            log.debug("Prepare Document");
            Document doc = new Document("name", "MongoDB")
                    .append("type", "database")
                    .append("count", 1)
                    .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                    .append("info", new Document("x", 203).append("y", 102));

            log.debug("getPublisher");
            Publisher<InsertOneResult> publisher = collection.insertOne(doc);

            log.debug("trying subscribe");
            publisher.subscribe(new Subscriber<>() {
                @Override
                public void onSubscribe(final Subscription s) {
                    s.request(1);  // <--- Data requested and the insertion will now occur
                    System.out.println("Inserted: onSubscribe occurs");
                }

                @Override
                public void onNext(final InsertOneResult result) {
                    System.out.println("Inserted: " + result);
                }

                @Override
                public void onError(final Throwable t) {
                    System.out.println("Failed: onError occurs: " + t.getMessage());
                    t.printStackTrace();
                }

                @Override
                public void onComplete() {
                    System.out.println("Completed");
                }
            });

            Thread.sleep(1000);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void simpleFluxExample() {
        Flux<String> fluxFruits = Flux.just("apple", "pear", "plum", "plum", "plum");
        Flux<String> fluxColors = Flux.just("red", "green", "blue", "plum", "plum");
        Flux<Integer> fluxAmounts = Flux.just(10, 20, 30, 1, 1);

        fluxColors.log().subscribe(System.out::println);
        fluxColors.subscribe(System.out::println);
        fluxColors.map(color -> color.charAt(0)).subscribe(System.out::println);
        Flux.zip(fluxFruits, fluxColors, fluxAmounts).subscribe(System.out::println);
    }

    public void backpressureExample() {
        Flux.range(1,5)
                .subscribe(new Subscriber<Integer>() {
                    private Subscription s;
                    int counter;

                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        this.s = s;
                        System.out.println("Requesting 2 emissions");
                        s.request(2);
                    }

                    @Override
                    public void onNext(Integer i) {
                        System.out.println("onNext " + i);
                        counter++;
                        if (counter % 2 == 0) {
                            System.out.println("Requesting 2 emissions");
                            s.request(2);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    public void coldPublisherExample() throws InterruptedException {
        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1));
        Thread.sleep(2000);
        intervalFlux.subscribe(i -> System.out.printf("Subscriber A, value: %d%n", i));
        Thread.sleep(2000);
        intervalFlux.subscribe(i -> System.out.printf("Subscriber B, value: %d%n", i));
        Thread.sleep(3000);
        /*
        Теперь вы можете задаться вопросом, почему что-то происходит, когда основной поток спит, но это потому,
        что оператор интервала по умолчанию выполняется в планировщике Schedulers.parallel(). Как видите,
        оба подписчика получат значения, начинающиеся с 0.
         */
    }

    public void hotPublisherExample() throws InterruptedException {
        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1));
        ConnectableFlux<Long> intervalCF = intervalFlux.publish();
        intervalCF.connect();
        Thread.sleep(2000);
        intervalCF.subscribe(i -> System.out.println(String.format("Subscriber A, value: %d", i)));
        Thread.sleep(2000);
        intervalCF.subscribe(i -> System.out.println(String.format("Subscriber B, value: %d", i)));
        Thread.sleep(3000);
    }

}
