package com.mateusz.jakuszko.roomforyou.opencagegeocoderapi;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenCageGeocoderClient {
    private final OpenCageGeocoderConfig openCageGeocoderConfig;
    private final JsonMapper jsonMapper;
    private final PolishLettersChanger lettersChanger;

    public Map<String, String> getGeometryValues(ApartmentDto apartmentDto) throws IOException, ParseException {
        StringBuilder url = new StringBuilder();
        url.append(openCageGeocoderConfig.getUrl()).append(openCageGeocoderConfig.getVersion()).append("/json?q=");
        if (apartmentDto.getStreetNumber() != null) {
            url.append(lettersChanger.changePolishLettersToEnglish(apartmentDto.getStreetNumber()))
                    .append(openCageGeocoderConfig.getBreakpoint());
        }
        if (apartmentDto.getStreetNumber() != null) {
            url.append(lettersChanger.changePolishLettersToEnglish(apartmentDto.getStreet()))
                    .append(openCageGeocoderConfig.getBreakpoint());
        }
        if (apartmentDto.getCity() != null) {
            url.append(lettersChanger.changePolishLettersToEnglish(apartmentDto.getCity()))
                    .append(openCageGeocoderConfig.getBreakpoint());
        }
        url.append("Polska");
        url.append("&key=").append(openCageGeocoderConfig.getKey());
        url.append("&no_annotations=1");

        return jsonMapper.mapJsonRespondToGeometryValues(new URL(url.toString()));
    }
}
