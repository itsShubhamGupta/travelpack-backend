package com.travelpack.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Package extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String location;

    private Double price;

    private Integer duration;

    @Column(length = 1000)
    private String description;

    private Integer availableSlots;

    private String imageUrl; // 🔥 store cloudinary URL
    @OneToMany(mappedBy = "pkg",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<Itinerary> itineraries;

}
