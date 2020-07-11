package com.mateusz.jakuszko.roomforyou.opencagegeocoderapi;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "open.cage.geocode.api")
@Getter
@Setter
public class OpenCageGeocoderConfig {
    private String key;
    private String url;
    private String version;
    private String breakpoint;
}
