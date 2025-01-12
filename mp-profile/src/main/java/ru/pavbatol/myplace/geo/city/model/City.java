package ru.pavbatol.myplace.geo.city.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.pavbatol.myplace.geo.IdableNameableGeo;
import ru.pavbatol.myplace.geo.district.model.District;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "cities")
public class City implements IdableNameableGeo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "district_id", nullable = false)
    District district;

    @Column(name = "name", nullable = false)
    String name;
}
