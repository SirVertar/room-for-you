package com.mateusz.jakuszko.roomforyou.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureDto {
    private BigDecimal temp;
}
