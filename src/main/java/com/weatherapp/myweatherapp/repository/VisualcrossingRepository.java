package com.weatherapp.myweatherapp.repository;

import com.weatherapp.myweatherapp.model.CityInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class VisualcrossingRepository {

    @Value("${weather.visualcrossing.url}")
    private String url;

    @Value("${weather.visualcrossing.key}")
    private String key;

    public CityInfo getByCity(String city) {
        String uri = url + "timeline/" + city + "?unitGroup=metric&key=" + key;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, CityInfo.class);
    }
}
