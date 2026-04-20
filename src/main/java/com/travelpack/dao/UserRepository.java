package com.travelpack.dao;

import com.travelpack.entity.Itinerary;
import com.travelpack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

        List<Itinerary> findByPkgId(Long packageId);
    }
}
