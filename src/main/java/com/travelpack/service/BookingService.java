package com.travelpack.service;

import com.travelpack.dao.BookingRepository;
import com.travelpack.dao.PackageRepository;
import com.travelpack.dao.UserRepository;
import com.travelpack.dto.BookingRequest;
import com.travelpack.dto.BookingResponse;
import com.travelpack.dto.PackageSummary;
import com.travelpack.entity.Booking;
import com.travelpack.entity.Package;
import com.travelpack.entity.User;
import com.travelpack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {


    private final BookingRepository bookingRepo;
    private final PackageRepository packageRepo;
    private final UserRepository userRepo;

    public BookingResponse book(Long userId, BookingRequest request) {

        // 1. Get user
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Get package
        // 2. Get package
        Package pkg = packageRepo.findById(request.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
        // 🔥 3. Check slots
        if (pkg.getAvailableSlots() <= 0) {
            throw new RuntimeException("No slots available");
        }

        // 🔥 4. Reduce slot
        pkg.setAvailableSlots(pkg.getAvailableSlots() - 1);

        // 5. Create booking
        Booking booking = Booking.builder()
                .user(user)
                .pkg(pkg)
                .bookingDate(LocalDateTime.now())
                .status("CONFIRMED")
                .build();

        // 6. Save
        packageRepo.save(pkg);
        bookingRepo.save(booking);

        return new BookingResponse(
                booking.getId(),
                booking.getStatus(),
                booking.getBookingDate(),
                new PackageSummary(
                        booking.getPkg().getId(),
                        booking.getPkg().getTitle(),
                        booking.getPkg().getPrice(),
                        booking.getPkg().getImageUrl()
                )
        );// update slot
    }

    public List<BookingResponse> getUserBookings(Long userId) {
        List<Booking> bookings = bookingRepo.findByUserId(userId);

        return bookings.stream()
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getStatus(),
                        b.getBookingDate(),
                        new PackageSummary(
                                b.getPkg().getId(),
                                b.getPkg().getTitle(),
                                b.getPkg().getPrice(),
                                b.getPkg().getImageUrl()
                        )
                ))
                .toList();
    }
}