package com.FoodDeliveryWebApp.Repository;

import com.FoodDeliveryWebApp.Entity.Subscription;
import com.FoodDeliveryWebApp.Entity.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

        List<Subscription> findByUserId(Long userId);
        List<Subscription> findByRestaurant_RestaurantId(Long restaurantId);
        List<Subscription> findByStatus(SubscriptionStatus status);
    }

