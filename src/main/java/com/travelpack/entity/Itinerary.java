package com.travelpack.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Itinerary extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer dayNumber;

    @Column(length = 1000)
    private String details;

    @ManyToOne
    @JoinColumn(name = "package_id")
    @JsonBackReference
    private Package pkg;
}
