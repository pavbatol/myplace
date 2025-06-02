package ru.pavbatol.myplace.geo.district.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.pavbatol.myplace.geo.common.IdableNameableGeo;
import ru.pavbatol.myplace.geo.region.model.Region;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "districts")
public class District implements IdableNameableGeo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "region_id", nullable = false)
    Region region;

    @Column(name = "name", nullable = false)
    String name;
}
