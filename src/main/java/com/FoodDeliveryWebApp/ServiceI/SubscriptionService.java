package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.Subscription;
import com.FoodDeliveryWebApp.Entity.SubscriptionStatus;
import com.FoodDeliveryWebApp.Entity.SubscriptionType;

import java.util.List;
import java.util.Map;

public interface SubscriptionService {
    Subscription createSubscription(Subscription subscription);
    List<Subscription> getSubscriptionsByUser(Long userId);
    List<Subscription> getSubscriptionsByRestaurant(Long restaurantId);
    Subscription updateSubscriptionStatus(Long subscriptionId, Subscription status);
    void cancelSubscription(Long subscriptionId);

    List<Map<String, Object>> getSubscriptionsByRestaurantAndUser(Long restaurantId, Long userId);
}
