package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.Subscription;
import com.FoodDeliveryWebApp.Entity.SubscriptionStatus;
import com.FoodDeliveryWebApp.Entity.SubscriptionType;
import com.FoodDeliveryWebApp.Repository.SubscriptionRepository;
import com.FoodDeliveryWebApp.ServiceI.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription createSubscription(Subscription subscription) {
        // Ensure startDate is provided by the frontend
        LocalDate startDate = subscription.getStartDate();
        if (startDate == null) {
            throw new IllegalArgumentException("Start date must be provided");
        }

        // Calculate end date based on the subscription type
        if (subscription.getSubscriptionType() == SubscriptionType.WEEKLY) {
            subscription.setEndDate(startDate.plusDays(6)); // Exactly 7 days
        } else if (subscription.getSubscriptionType() == SubscriptionType.MONTHLY) {
            subscription.setEndDate(startDate.plusDays(29)); // Exactly 30 days
        }

        // Set the subscription status to ACTIVE by default
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        // Save the subscription to the repository
        return subscriptionRepository.save(subscription);
    }

    @Override
    public List<Subscription> getSubscriptionsByUser(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public List<Subscription> getSubscriptionsByRestaurant(Long restaurantId) {
        return subscriptionRepository.findByRestaurant_RestaurantId(restaurantId);
    }

    @Override
    public Subscription updateSubscriptionStatus(Long subscriptionId, Subscription status) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscription.setStatus(status.getStatus());
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void cancelSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscriptionRepository.save(subscription);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void checkSubscriptions() {
        List<Subscription> activeSubscriptions = subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);
        LocalDate today = LocalDate.now();
        for (Subscription subscription : activeSubscriptions) {
            if (subscription.getEndDate().isBefore(today)) {
                subscription.setStatus(SubscriptionStatus.COMPLETED);
                subscriptionRepository.save(subscription);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getSubscriptionsByRestaurantAndUser(Long restaurantId, Long userId) {
        // Validate inputs
        if (restaurantId == null || userId == null) {
            throw new IllegalArgumentException("Restaurant ID and User ID must not be null");
        }
            List<Subscription> subscriptions=subscriptionRepository.findAll();

        List<Map<String,Object>> responseList=new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            Map<String, Object> response = Map.of(
                    "subscriptionId", subscription.getId(),
                    "restaurantId", subscription.getRestaurant().getRestaurantId(),
                    "userName", subscription.getUser().getUsername(),
                    "startDate", subscription.getStartDate(),
                    "endDate", subscription.getEndDate(),
                    "status", subscription.getStatus().name()
            );
            responseList.add(response);
        }

        return responseList;
    }

}
