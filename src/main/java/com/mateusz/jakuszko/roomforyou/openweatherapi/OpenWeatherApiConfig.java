package com.mateusz.jakuszko.roomforyou.openweatherapi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "open.weather.map.api")
@Getter
@Setter
public class OpenWeatherApiConfig {
    String key;
    String url;
}
