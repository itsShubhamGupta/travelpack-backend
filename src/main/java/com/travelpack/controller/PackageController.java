package com.travelpack.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.travelpack.dto.PackageRequest;
import com.travelpack.entity.Package;
import com.travelpack.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
public class PackageController {

    @Autowired
    private  PackageService packageService;

    // 🔐 ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/package")
    public Package add(@ModelAttribute  PackageRequest request) throws JsonProcessingException {
        return packageService.addPackage(request);
    }

    // 🌍 PUBLIC / USER
    @GetMapping("/packages")
    public List<Package> getAll( @RequestParam(required = false) String location,
                                 @RequestParam(required = false) Double minPrice,
                                 @RequestParam(required = false) Double maxPrice) {
        return packageService.getAllPackages(location, minPrice, maxPrice);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/package/{id}")
    public Package getById(@PathVariable Long id) {
        return packageService.getById(id);
    }
}
