package com.travelpack.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelpack.dao.PackageRepository;
import com.travelpack.dto.ItineraryRequest;
import com.travelpack.dto.PackageRequest;
import com.travelpack.entity.Itinerary;
import com.travelpack.entity.Package;
import com.travelpack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageService {

    @Autowired
    private  PackageRepository packageRepository;
    @Autowired
    private  ImageService imageService;

    // 🔥 ADMIN
    public Package addPackage(PackageRequest request) throws JsonProcessingException {

        String imageUrl = imageService.uploadImage(request.getImage());

        Package pkg = Package.builder()
                .title(request.getTitle())
                .location(request.getLocation())
                .price(request.getPrice())
                .duration(request.getDuration())
                .description(request.getDescription())
                .availableSlots(request.getAvailableSlots())
                .imageUrl(imageUrl)
                .build();

        // 🔥 3. Convert itinerary JSON → Object
        ObjectMapper mapper = new ObjectMapper();

        List<ItineraryRequest> list =
                mapper.readValue(request.getItineraries(),
                        new TypeReference<List<ItineraryRequest>>() {});

        // 🔥 4. Map itinerary
        List<Itinerary> itineraryList = list.stream()
                .map(i -> Itinerary.builder()
                        .dayNumber(i.getDayNumber())
                        .details(i.getDetails())
                        .pkg(pkg) // IMPORTANT
                        .build())
                .toList();

        pkg.setItineraries(itineraryList);

        // 🔥 5. Save

        return packageRepository.save(pkg);
    }

    // 🔥 USER
    public List<Package> getAllPackages(String location,
                                        Double minPrice,
                                        Double maxPrice) {
        return packageRepository.searchPackages(location, minPrice, maxPrice);
    }

    public Package getById(Long id) {
        return packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
    }
}
