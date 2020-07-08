package com.mateusz.jakuszko.roomforyou.opencagegeocoder.client;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.OpenCageGeocoder.GetResponse;
import com.mateusz.jakuszko.roomforyou.opencagegeocoder.configuration.OpenCageGeocoderConfig;
import com.mateusz.jakuszko.roomforyou.opencagegeocoder.mapper.JsonMapper;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Component
@AllArgsConstructor
public class OpenCageGeocoderClient {

    private final OpenCageGeocoderConfig openCageGeocoderConfig;
    private final JsonMapper jsonMapper;
    private RestTemplate restTemplate;

    public Map<String, String> getGeometryValues(ApartmentDto apartment) throws IOException, ParseException {
        StringBuilder url = new StringBuilder();
        url.append(openCageGeocoderConfig.getUrl()).append(openCageGeocoderConfig.getVersion()).append("/json?q=");
        if (apartment.getStreetNumber() != null) {
            url.append(apartment.getStreetNumber()).append(openCageGeocoderConfig.getBreakpoint());
        }
        if (apartment.getStreetNumber() != null) {
            url.append(apartment.getStreet()).append(openCageGeocoderConfig.getBreakpoint());
        }
        if (apartment.getCity() != null) {
            url.append(apartment.getCity()).append(openCageGeocoderConfig.getBreakpoint());
        }
        url.append("Polska");
        url.append("&key=").append(openCageGeocoderConfig.getKey());
        url.append("&no_annotations=1");

        return jsonMapper.mapJsonRespondToGeometryValues(new URL(url.toString()));
    }

    //TODO Resolve problem with wrong response
    public GetResponse getResponse(ApartmentDto apartment) {
        StringBuilder url = new StringBuilder();
        url.append(openCageGeocoderConfig.getUrl()).append(openCageGeocoderConfig.getVersion()).append("/json?q=");
        if (apartment.getStreetNumber() != null) {
            url.append(apartment.getStreetNumber()).append(openCageGeocoderConfig.getBreakpoint());
        }
        if (apartment.getStreetNumber() != null) {
            url.append(apartment.getStreet()).append(openCageGeocoderConfig.getBreakpoint());
        }
        if (apartment.getCity() != null) {
            url.append(apartment.getCity()).append(openCageGeocoderConfig.getBreakpoint());
        }
        url.append("Polska");
        url.append("&key=").append(openCageGeocoderConfig.getKey());
        url.append("&no_annotations=1");

        return jsonMapper.getResponse(url.toString());
    }

}
