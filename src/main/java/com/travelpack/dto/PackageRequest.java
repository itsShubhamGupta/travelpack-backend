package com.travelpack.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@Getter
@Setter
public class PackageRequest {

    private String title;
    private String location;
    private Double price;
    private Integer duration;
    private String description;
    private Integer availableSlots;

    private MultipartFile image; // 🔥 file input
    private String itineraries; // JSON string 🔥

}
