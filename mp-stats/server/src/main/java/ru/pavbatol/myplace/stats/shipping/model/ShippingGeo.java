package ru.pavbatol.myplace.stats.shipping.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@Document(collection = "shippingGeos")
public class ShippingGeo {
    @Id
    String id;
    Long itemId;
    String country;
    String city;
    LocalDateTime timestamp;
}
