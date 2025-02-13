package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {

  @InjectMocks
  private WeatherService weatherService;

  @Mock
  private VisualcrossingRepository weatherRepo;

  private CityInfo mockCityInfo1;
  private CityInfo mockCityInfo2;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    mockCityInfo1 = new CityInfo();
    CityInfo.CurrentConditions conditions1 = new CityInfo.CurrentConditions();
    conditions1.setSunrise("06:30");
    conditions1.setSunset("18:30");
    conditions1.setConditions("Clear");
    mockCityInfo1.setCurrentConditions(conditions1);

    mockCityInfo2 = new CityInfo();
    CityInfo.CurrentConditions conditions2 = new CityInfo.CurrentConditions();
    conditions2.setSunrise("07:00");
    conditions2.setSunset("19:00");
    conditions2.setConditions("Rainy");
    mockCityInfo2.setCurrentConditions(conditions2);
  }

  @Test
  void testCompareDaylightHours() {
    when(weatherRepo.getByCity("London")).thenReturn(mockCityInfo1);
    when(weatherRepo.getByCity("New York")).thenReturn(mockCityInfo2);

    String result = weatherService.compareDaylightHours("London", "New York");
    assertEquals("New York has a longer day", result);
  }

  @Test
  void testCheckRain() {
    when(weatherRepo.getByCity("London")).thenReturn(mockCityInfo1);
    when(weatherRepo.getByCity("New York")).thenReturn(mockCityInfo2);

    String result = weatherService.checkRain("London", "New York");
    assertEquals("New York is currently experiencing rain.", result);
  }
}
