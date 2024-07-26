package com.FoodDeliveryWebApp.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import com.FoodDeliveryWebApp.Entity.User;
@Entity
@Table(name = "Location")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private Double latitude;

    private Double longitude;


    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    @JsonIgnoreProperties("locations")
    private User user;

}

