package com.example.WeatherInfo.dto;

import lombok.Data;

@Data
public class GeoResponseDTO {
    private String zip;
    private String name;
    private double lat;
    private double lon;
    private String country;
}
