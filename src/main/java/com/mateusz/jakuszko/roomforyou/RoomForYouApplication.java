package com.mateusz.jakuszko.roomforyou;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class RoomForYouApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomForYouApplication.class, args);
    }
}
