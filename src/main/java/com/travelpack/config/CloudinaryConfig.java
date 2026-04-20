package com.travelpack.config;


import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(Map.of(
                "cloud_name", "dskrrjcyz",
                "api_key", "525823966948214",
                "api_secret", "wibLx1sxxpIY1KLr8DZGUQ82WPQ"
        ));
    }
}
