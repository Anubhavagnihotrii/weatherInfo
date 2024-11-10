package com.example.WeatherInfo.service;

import com.example.WeatherInfo.dto.GeoResponseDTO;
import com.example.WeatherInfo.dto.WeatherResponseDTO;
import com.example.WeatherInfo.model.PincodeData;
import com.example.WeatherInfo.repository.PincodeDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class WeatherService {

    @Autowired
    private PincodeDataRepository pincodeDataRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${weather.api.key}")
    private String apiKey;

    public String getWeatherInfo(int pincode, LocalDate forDate) {
        Optional<PincodeData> existingData = pincodeDataRepository.findByPincodeAndForDate(pincode, forDate);
        if (existingData.isPresent()) {
            return existingData.get().getWeatherInfo();
        }

        GeoResponseDTO geoData = fetchLatLong(pincode);
        if (geoData == null) {
            throw new RuntimeException("Failed to retrieve latitude and longitude for pincode: " + pincode);
        }

        String lat = String.valueOf(geoData.getLat());
        String lon = String.valueOf(geoData.getLon());
        WeatherResponseDTO weatherData = fetchWeather(lat, lon);
        String weatherInfo = convertToJson(weatherData);

        savePincodeData(pincode, lat, lon, weatherInfo, forDate);

        return weatherInfo;
    }

    private GeoResponseDTO fetchLatLong(int pincode) {
        String geoUrl = UriComponentsBuilder.fromHttpUrl("http://api.openweathermap.org/geo/1.0/zip")
                .queryParam("zip", pincode + ",IN")
                .queryParam("appid", apiKey)
                .toUriString();

        return restTemplate.getForObject(geoUrl, GeoResponseDTO.class);
    }

    private WeatherResponseDTO fetchWeather(String lat, String lon) {
        String weatherUrl = UriComponentsBuilder.fromHttpUrl("http://api.openweathermap.org/data/2.5/weather")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", apiKey)
                .toUriString();

        WeatherResponseDTO weatherResponse = restTemplate.getForObject(weatherUrl, WeatherResponseDTO.class);
        if (weatherResponse == null) {
            throw new RuntimeException("Failed to retrieve weather data for coordinates: " + lat + ", " + lon);
        }
        return weatherResponse;
    }

    private String convertToJson(WeatherResponseDTO weatherData) {
        try {
            return objectMapper.writeValueAsString(weatherData);
        } catch (Exception e) {
            throw new RuntimeException("Error converting weather data to JSON", e);
        }
    }

    private void savePincodeData(int pincode, String latitude, String longitude, String weatherInfo, LocalDate forDate) {
        PincodeData data = new PincodeData();
        data.setPincode(pincode);
        data.setLatitude(Double.valueOf(latitude));
        data.setLongitude(Double.valueOf(longitude));
        data.setWeatherInfo(weatherInfo);
        data.setForDate(forDate);
        pincodeDataRepository.save(data);
    }
}
