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
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription createSubscription(Subscription subscription) {
        subscription.setStartDate(LocalDate.now());
        if (subscription.getSubscriptionType() == SubscriptionType.WEEKLY) {
            subscription.setEndDate(subscription.getStartDate().plusWeeks(1));
        } else if (subscription.getSubscriptionType() == SubscriptionType.MONTHLY) {
            subscription.setEndDate(subscription.getStartDate().plusMonths(1));
        }
        subscription.setStatus(SubscriptionStatus.ACTIVE);
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

}
