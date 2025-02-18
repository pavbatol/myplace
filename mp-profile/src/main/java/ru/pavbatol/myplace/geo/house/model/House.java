package ru.pavbatol.myplace.geo.house.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.pavbatol.myplace.geo.IdableNameableGeo;
import ru.pavbatol.myplace.geo.street.model.Street;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "houses")
public class House implements IdableNameableGeo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "house_id", nullable = false)
    Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "street_id", nullable = false)
    Street street;

    @Column(name = "number", nullable = false)
    String number;

    @Column(name = "lat")
    double lat;

    @Column(name = "lon")
    double lon;

    @Override
    public String getName() {
        return number;
    }
}
