package com.example.WeatherInfo.service;

import com.example.WeatherInfo.dto.GeoResponseDTO;
import com.example.WeatherInfo.dto.WeatherResponseDTO;
import com.example.WeatherInfo.model.PincodeData;
import com.example.WeatherInfo.repository.PincodeDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {
    @Mock
    PincodeDataRepository pincodeDataRepository;
    @Mock
    RestTemplate restTemplate ;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    private WeatherService weatherService;

    private final int testPincode =560036;
    private final LocalDate testDate = LocalDate.now();

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWeatherInfo_WhenDataExistsInDatabase(){
        PincodeData mockData = new PincodeData();
        mockData.setWeatherInfo("Mocked weather info");

        when(pincodeDataRepository.findByPincodeAndForDate(testPincode,testDate)).thenReturn(Optional.of(mockData));

        String result = weatherService.getWeatherInfo(testPincode,testDate);
        assertEquals("Mocked weather info",result);
        verify(pincodeDataRepository,times(1)).findByPincodeAndForDate(testPincode,testDate);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void testGetWeatherInfo_WhenDataNotInDatabase() throws Exception {

        when(pincodeDataRepository.findByPincodeAndForDate(testPincode,testDate)).thenReturn(Optional.empty());

        GeoResponseDTO geoResponseDTO = new GeoResponseDTO();
        geoResponseDTO.setLat(12.34);
        geoResponseDTO.setLon(56.78);
        when(restTemplate.getForObject(anyString(),eq(GeoResponseDTO.class))).thenReturn(geoResponseDTO);

        WeatherResponseDTO weatherResponseDTO = new WeatherResponseDTO();
        when(restTemplate.getForObject(anyString(),eq(WeatherResponseDTO.class))).thenReturn(weatherResponseDTO);

        String mockWeatherJson = "{\"mock\": \"data\"}";
        when(objectMapper.writeValueAsString(any(WeatherResponseDTO.class))).thenReturn(mockWeatherJson);

        String result = weatherService.getWeatherInfo(testPincode,testDate);

        assertEquals(mockWeatherJson,result);
        verify(pincodeDataRepository,times(1)).findByPincodeAndForDate(testPincode,testDate);
        verify(restTemplate,times(2)).getForObject(anyString(),any());
        verify(pincodeDataRepository,times(1)).save(any(PincodeData.class));
    }
}
