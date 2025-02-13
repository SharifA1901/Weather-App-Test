package com.weatherapp.myweatherapp.controller;

import com.weatherapp.myweatherapp.exception.WeatherServiceException;
import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherService weatherService;

    /**
     * Endpoint to fetch weather forecast data for a specific city.
     * @param city The name of the city.
     * @return ResponseEntity containing weather details.
     */
    @GetMapping("/forecast/{city}")
    public ResponseEntity<?> forecastByCity(@PathVariable("city") String city) {
        try {
            CityInfo ci = weatherService.forecastByCity(city);
            return ResponseEntity.ok(ci);
        } catch (WeatherServiceException e) {
            logger.error("Error fetching forecast for {}", city, e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Endpoint to compare daylight hours between two cities.
     * @param city1 Name of the first city.
     * @param city2 Name of the second city.
     * @return ResponseEntity with the result of daylight comparison.
     */
    @GetMapping("/daylight/{city1}/{city2}")
    public ResponseEntity<String> compareDaylight(@PathVariable String city1, @PathVariable String city2) {
        try {
            return ResponseEntity.ok(weatherService.compareDaylightHours(city1, city2));
        } catch (WeatherServiceException e) {
            logger.error("Error comparing daylight for {} and {}", city1, city2, e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Endpoint to check which city (if any) is currently experiencing rain.
     * @param city1 Name of the first city.
     * @param city2 Name of the second city.
     * @return ResponseEntity with the rain check result.
     */
    @GetMapping("/raincheck/{city1}/{city2}")
    public ResponseEntity<String> checkRain(@PathVariable String city1, @PathVariable String city2) {
        try {
            return ResponseEntity.ok(weatherService.checkRain(city1, city2));
        } catch (WeatherServiceException e) {
            logger.error("Error checking rain conditions for {} and {}", city1, city2, e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        logger.error("Unexpected error: ", e);
        return ResponseEntity.status(500).body("An unexpected error occurred.");
    }
}
