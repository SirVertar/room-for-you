package com.mateusz.jakuszko.roomforyou.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private String mailTo;
    private String subject;
    private String message;
    private String toCc;

}
