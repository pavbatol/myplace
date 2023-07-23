package ru.pavbatol.myplace.stats.view.repository;

import reactor.core.publisher.Flux;
import ru.pavbatol.myplace.dto.view.ViewSearchFilter;
import ru.pavbatol.myplace.stats.view.model.View;

import java.time.LocalDateTime;

public class CustomViewMongoRepositoryImpl implements CustomViewMongoRepository {
    @Override
    public Flux<View> find(ViewSearchFilter filter) {
        View view = new View()
                .setTimestamp(LocalDateTime.now())
                .setApp("app")
                .setUri("uri")
                .setIp("ip")
                .setId("id");

        View view2 = new View()
                .setTimestamp(LocalDateTime.now())
                .setApp("app2")
                .setUri("uri2")
                .setIp("ip2")
                .setId("id2");

        return Flux.just(view, view2);

    }
}
