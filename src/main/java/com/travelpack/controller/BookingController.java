package com.travelpack.controller;

import com.travelpack.dto.BookingRequest;
import com.travelpack.dto.BookingResponse;
import com.travelpack.entity.Booking;
import com.travelpack.entity.User;
import com.travelpack.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/booking")
    public BookingResponse book(@RequestBody BookingRequest request,
                                Authentication auth) {

        User user = (User) auth.getPrincipal();

        return service.book(user.getId(), request);
    }

    @GetMapping("/booking")
    public List<BookingResponse> myBookings(Authentication auth) {

        User user = (User) auth.getPrincipal();

        return service.getUserBookings(user.getId());
    }
}
