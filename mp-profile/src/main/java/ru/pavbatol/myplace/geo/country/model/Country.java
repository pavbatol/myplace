package ru.pavbatol.myplace.geo.country.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.pavbatol.myplace.geo.common.IdableNameableGeo;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "countries")
public class Country implements IdableNameableGeo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    Long id;

    @Column(name = "code", nullable = false)
    String code;

    @Column(name = "name", nullable = false)
    String name;
}
