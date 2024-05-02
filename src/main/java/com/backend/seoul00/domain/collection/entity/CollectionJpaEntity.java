package com.backend.seoul00.domain.collection.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "collection")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double longitude;

    private Double latitude;

    private String address;

    @Enumerated(EnumType.STRING)
    private Type type;

    public CollectionJpaEntity(String address, Type type) {
        this.address = address;
        this.type = type;
    }

    public void updatePoint(String longitude, String latitude) {
        this.longitude = toDouble(longitude);
        this.latitude = toDouble(latitude);
    }

    private Double toDouble(String str) {
        str = str.replaceAll("\"", "");
        return Double.parseDouble(str);
    }
}
