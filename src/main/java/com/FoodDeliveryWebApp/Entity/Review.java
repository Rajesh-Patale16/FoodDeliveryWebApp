package com.FoodDeliveryWebApp.Entity;

import com.FoodDeliveryWebApp.Entity.Menu;
import com.FoodDeliveryWebApp.Entity.Restaurant;
import com.FoodDeliveryWebApp.Entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "restaurant", "menu"})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference(value = "user-reviews")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = true)
    @JsonBackReference(value = "restaurant-reviews")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = true)
    @JsonBackReference(value = "menu-reviews")
    private Menu menu;

    private String reviewType;
    private int rating;
    private String comment;
    private LocalDate reviewDate;
}
