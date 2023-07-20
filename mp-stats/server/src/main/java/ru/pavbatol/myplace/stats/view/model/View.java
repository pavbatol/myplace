package ru.pavbatol.myplace.stats.view.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "views")
public class View {
    @Id
    String id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
