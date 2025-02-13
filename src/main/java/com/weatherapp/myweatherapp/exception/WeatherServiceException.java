package com.weatherapp.myweatherapp.exception;

/**
 * Custom exception class for handling weather service-related errors.
 */
public class WeatherServiceException extends RuntimeException {
    public WeatherServiceException(String message) {
        super(message);
    }

    public WeatherServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
