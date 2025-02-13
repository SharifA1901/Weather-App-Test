package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.exception.WeatherServiceException;
import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Autowired
    private VisualcrossingRepository weatherRepo;

    /**
     * Fetches weather forecast data for a given city.
     * @param city The name of the city.
     * @return CityInfo object containing weather details.
     */
    public CityInfo forecastByCity(String city) {
        try {
            CityInfo cityInfo = weatherRepo.getByCity(city);
            if (cityInfo == null) {
                throw new WeatherServiceException("Weather data unavailable for " + city);
            }
            return cityInfo;
        } catch (Exception e) {
            logger.error("Failed to fetch weather forecast for city: {}", city, e);
            throw new WeatherServiceException("Unable to retrieve forecast for " + city);
        }
    }

    /**
     * Compares daylight hours between two cities and returns the city with the longest daylight period.
     * @param city1 First city name.
     * @param city2 Second city name.
     * @return A string indicating which city has a longer daylight duration.
     */
    public String compareDaylightHours(String city1, String city2) {
        try {
            CityInfo info1 = weatherRepo.getByCity(city1);
            CityInfo info2 = weatherRepo.getByCity(city2);

            // Check if weather data is available
            if (info1 == null || info2 == null || info1.getCurrentConditions() == null || info2.getCurrentConditions() == null) {
                throw new WeatherServiceException("Weather data unavailable for one or both cities.");
            }

            // Calculate daylight hours
            long daylight1 = calculateDaylightHours(info1.getCurrentConditions().getSunrise(), info1.getCurrentConditions().getSunset());
            long daylight2 = calculateDaylightHours(info2.getCurrentConditions().getSunrise(), info2.getCurrentConditions().getSunset());

            return (daylight1 > daylight2) ? city1 + " has a longer day" : city2 + " has a longer day";
        } catch (WeatherServiceException e) {
            logger.error("Error in comparing daylight for {} and {}: {}", city1, city2, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error comparing daylight hours for {} and {}", city1, city2, e);
            throw new WeatherServiceException("Error processing daylight comparison.");
        }
    }

    /**
     * Parses sunrise and sunset times and calculates the number of daylight hours.
     * Handles different time formats (HH:mm and HH:mm:ss).
     * @param sunrise Time of sunrise.
     * @param sunset Time of sunset.
     * @return The duration of daylight in hours.
     */
    private long calculateDaylightHours(String sunrise, String sunset) {
        try {
            if (sunrise == null || sunset == null) {
                throw new WeatherServiceException("Missing sunrise or sunset data.");
            }

            // Determine time format (HH:mm or HH:mm:ss)
            DateTimeFormatter formatter = sunrise.length() > 5 ? 
                DateTimeFormatter.ofPattern("HH:mm:ss") : DateTimeFormatter.ofPattern("HH:mm");

            LocalTime sunriseTime = LocalTime.parse(sunrise, formatter);
            LocalTime sunsetTime = LocalTime.parse(sunset, formatter);

            return java.time.Duration.between(sunriseTime, sunsetTime).toHours();
        } catch (DateTimeParseException e) {
            logger.error("Error parsing time values: sunrise={}, sunset={}", sunrise, sunset, e);
            throw new WeatherServiceException("Invalid time format received from API.");
        }
    }

    /**
     * Checks which of the two cities is currently experiencing rain.
     * @param city1 First city name.
     * @param city2 Second city name.
     * @return A string indicating which city (if any) is experiencing rain.
     */
    public String checkRain(String city1, String city2) {
        try {
            CityInfo info1 = weatherRepo.getByCity(city1);
            CityInfo info2 = weatherRepo.getByCity(city2);

            // Check if weather data is available
            if (info1 == null || info2 == null || info1.getCurrentConditions() == null || info2.getCurrentConditions() == null) {
                throw new WeatherServiceException("Weather data unavailable for one or both cities.");
            }

            boolean isRainingCity1 = info1.getCurrentConditions().getConditions().toLowerCase().contains("rain");
            boolean isRainingCity2 = info2.getCurrentConditions().getConditions().toLowerCase().contains("rain");

            if (isRainingCity1 && isRainingCity2) {
                return "Both " + city1 + " and " + city2 + " are experiencing rain.";
            } else if (isRainingCity1) {
                return city1 + " is currently experiencing rain.";
            } else if (isRainingCity2) {
                return city2 + " is currently experiencing rain.";
            } else {
                return "Neither city is experiencing rain.";
            }
        } catch (WeatherServiceException e) {
            logger.error("Error checking rain conditions for {} and {}: {}", city1, city2, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error checking rain status for {} and {}", city1, city2, e);
            throw new WeatherServiceException("Error processing rain check.");
        }
    }
}
