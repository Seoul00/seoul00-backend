package com.backend.seoul00.domain.collection.dto;

import lombok.Data;

@Data
public class SearchResponseDto {
    private Double longitude;
    private Double latitude;
    private String address;

    public SearchResponseDto(Double longitude, Double latitude, String address) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }
}
