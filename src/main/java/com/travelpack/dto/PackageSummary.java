package com.travelpack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PackageSummary {

    private Long id;
    private String title;
    private Double price;
    private String imageUrl;
}
