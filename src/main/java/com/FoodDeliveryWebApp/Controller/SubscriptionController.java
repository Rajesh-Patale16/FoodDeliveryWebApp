package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.Subscription;
import com.FoodDeliveryWebApp.Entity.SubscriptionStatus;
import com.FoodDeliveryWebApp.Entity.SubscriptionType;
import com.FoodDeliveryWebApp.ServiceI.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@CrossOrigin("*")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/create")
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        Subscription createdSubscription = subscriptionService.createSubscription(subscription);
        return new ResponseEntity<>(createdSubscription, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByUser(@PathVariable Long userId) {
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByUser(userId);
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByRestaurant(@PathVariable Long restaurantId) {
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByRestaurant(restaurantId);
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @PutMapping("/update/{subscriptionId}")
    public ResponseEntity<Subscription> updateSubscriptionStatus(
            @PathVariable Long subscriptionId, @RequestBody Subscription status) {
        Subscription updatedSubscription = subscriptionService.updateSubscriptionStatus(subscriptionId, status);
        return new ResponseEntity<>(updatedSubscription, HttpStatus.OK);
    }

    @DeleteMapping("/cancel/{subscriptionId}")
    public ResponseEntity<String> cancelSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.cancelSubscription(subscriptionId);
        return  ResponseEntity.status(HttpStatus.OK).body("Subscription cancelled");
    }
}
