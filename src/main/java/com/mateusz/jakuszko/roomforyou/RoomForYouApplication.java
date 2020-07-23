package com.mateusz.jakuszko.roomforyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//
//@SpringBootApplication
//@EnableConfigurationProperties
//public class RoomForYouApplication {
//    public static void main(String[] args) {
//        SpringApplication.run(RoomForYouApplication.class, args);
//    }
//}

@SpringBootApplication
@EnableConfigurationProperties
public class RoomForYouApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(RoomForYouApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RoomForYouApplication.class);
    }
}
