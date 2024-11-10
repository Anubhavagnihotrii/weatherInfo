package com.example.WeatherInfo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class PincodeData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int pincode;

    private Double latitude;
    private Double longitude;

    @Column(columnDefinition = "JSON")
    private String weatherInfo;

    @Column(name = "for_date", nullable = false)
    private LocalDate forDate;
}
