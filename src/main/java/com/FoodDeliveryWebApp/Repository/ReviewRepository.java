package com.FoodDeliveryWebApp.Repository;

import com.FoodDeliveryWebApp.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRestaurantRestaurantId(Long restaurantId);
    List<Review> findByUserId(Long userId);
    List<Review> findByMenuMenuId(Long menuId);
}
