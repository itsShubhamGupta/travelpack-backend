package com.travelpack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingResponse {

    private Long id;
    private String status;
    private LocalDateTime bookingDate;

    private PackageSummary pkg;
}
