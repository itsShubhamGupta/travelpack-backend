package com.travelpack.dao;


import com.travelpack.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Long> {

    List<Package> findByLocationIgnoreCase(String location);

    @Query("""
        SELECT p FROM Package p
        WHERE (:location IS NULL OR LOWER(p.location) = LOWER(:location))
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        AND p.active = true
    """)
    List<Package> searchPackages(
            @Param("location") String location,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}
