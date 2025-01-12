package ru.pavbatol.myplace.geo.street.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.pavbatol.myplace.geo.IdableNameableGeo;
import ru.pavbatol.myplace.geo.city.model.City;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "streets")
public class Street implements IdableNameableGeo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "street_id", nullable = false)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", nullable = false)
    City city;

    @Column(name = "name", nullable = false)
    String name;
}
