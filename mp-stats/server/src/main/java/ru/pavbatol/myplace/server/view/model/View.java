package ru.pavbatol.myplace.server.view.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Document(collection = "views")
public class View {
    @Id
    String id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
