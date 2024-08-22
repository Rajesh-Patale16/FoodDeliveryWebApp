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
import java.util.Map;

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

    @GetMapping("/restaurant/{restaurantId}/user/{userId}")
    public ResponseEntity<?> getSubscriptionsByRestaurantAndUser(
            @PathVariable Long restaurantId, @PathVariable Long userId) {

        // Validate the input parameters
        if (restaurantId == null || userId == null) {
            return new ResponseEntity<>("Restaurant ID and User ID must not be null", HttpStatus.BAD_REQUEST);
        }

        // Fetch subscriptions based on restaurantId and userId
        List<Map<String,Object>> subscriptions = subscriptionService.getSubscriptionsByRestaurantAndUser(restaurantId, userId);

        // Handle the case where no subscriptions are found
        if (subscriptions.isEmpty()) {
            return new ResponseEntity<>("No subscriptions found for the given Restaurant ID and User ID", HttpStatus.NOT_FOUND);
        }

        // Return the list of subscriptions with an OK status
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

}
