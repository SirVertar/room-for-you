package com.mateusz.jakuszko.roomforyou.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TemperatureDto {
    private BigDecimal temp;
}
