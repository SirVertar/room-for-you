package com.mateusz.jakuszko.roomforyou.opencagegeocoderapi.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "lat",
        "lng"
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {
    @JsonProperty("lat")
    private Double lat;
    @JsonProperty("lng")
    private Double lng;
}
