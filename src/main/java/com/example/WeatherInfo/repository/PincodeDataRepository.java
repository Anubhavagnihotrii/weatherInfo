package com.example.WeatherInfo.repository;

import com.example.WeatherInfo.model.PincodeData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PincodeDataRepository extends JpaRepository<PincodeData,Long> {

    Optional<PincodeData> findByPincodeAndForDate(int pincode, LocalDate forDate);
}
