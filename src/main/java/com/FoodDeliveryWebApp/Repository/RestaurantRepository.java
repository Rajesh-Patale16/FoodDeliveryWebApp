package com.FoodDeliveryWebApp.Repository;

import com.FoodDeliveryWebApp.Entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

    Optional<Restaurant> findByRestaurantName(String restaurantName);
}
