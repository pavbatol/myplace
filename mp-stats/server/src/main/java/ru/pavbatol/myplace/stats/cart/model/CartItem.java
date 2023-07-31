package ru.pavbatol.myplace.stats.cart.model;

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
@Document(collection = "cartItems")
public class CartItem {
    @Id
    String id;
    Long userId;
    Long itemId;
    LocalDateTime timestamp;
}
