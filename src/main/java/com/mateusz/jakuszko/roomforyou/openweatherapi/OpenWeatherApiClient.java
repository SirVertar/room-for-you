package com.mateusz.jakuszko.roomforyou.openweatherapi;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.openweatherapi.dto.GetTemperatureResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenWeatherApiClient {
    private final OpenWeatherApiConfig openWeatherApiConfig;
    private final RestTemplate restTemplate;

    public GetTemperatureResponse getTemperatureResponse(ApartmentDto apartment) {
        log.info("Build URL to weather api for apartment with id - " + apartment.getId());
        URI url = buildUrl(apartment);
        return restTemplate.getForObject(url, GetTemperatureResponse.class);
    }

    private URI buildUrl(ApartmentDto apartment) {
        return UriComponentsBuilder.fromHttpUrl(openWeatherApiConfig.getUrl())
                .queryParam("lat", apartment.getLatitude())
                .queryParam("lon", apartment.getLongitude())
                .queryParam("appid", openWeatherApiConfig.getKey()).build().encode().toUri();
    }
}
