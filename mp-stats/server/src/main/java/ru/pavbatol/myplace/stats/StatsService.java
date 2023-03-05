package ru.pavbatol.myplace.stats;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@SpringBootApplication
public class StatsService {
    public static void main(String[] args) {
//        SpringApplication.run(StatsService.class, args);

        TestingMongo testingMongo = new TestingMongo();
//        testingMongo.setBase();
        testingMongo.setReactiveBase();
        System.out.println("!!!app started");
//        log.info("!!!app started - -1");
//        log.warn("!!!app started - 0");
//        log.debug("!!!app started - 1");
//        log.error("!!!app started - 2");
//        log.warn("!!!app started - 3");
//        log.debug("!!!app started - 4");
//        log.debug("!!!app started - 5");

    }

}
