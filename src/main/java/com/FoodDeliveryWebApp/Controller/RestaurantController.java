package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.Restaurant;
import com.FoodDeliveryWebApp.Exception.RestaurantNotFoundException;
import com.FoodDeliveryWebApp.ServiceI.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin("*")
public class RestaurantController {

    private final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    private RestaurantService restaurantService;

    // To save restaurant details
    @PostMapping("/restaurant/save")
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
        logger.info("Request to save restaurant: {}", restaurant);
        try {
            return ResponseEntity.ok(restaurantService.saveRestaurants(restaurant));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid restaurant data : {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Failed to save restaurant: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // To get restaurant details
    @GetMapping("/restaurant/find/{restaurantName}")
    public ResponseEntity<Restaurant> getRestaurantByName(@PathVariable("restaurantName") String restaurantName) {
        logger.info("Request to get restaurant by name: {}", restaurantName);
        try {
            return ResponseEntity.ok(restaurantService.getRestaurantsByName(restaurantName));
        } catch (RestaurantNotFoundException e) {
            logger.error("Restaurant not found : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Failed to get restaurant: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // To get all restaurant details
    @GetMapping("/restaurant/findAll")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        logger.info("Request to get all restaurants");
        try {
            return ResponseEntity.ok(restaurantService.getAllRestaurants());
        } catch (Exception e) {
            logger.error("Failed to get all restaurants: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // To update restaurant details
    @PutMapping("/restaurant/update/{restaurantId}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable("restaurantId") Long restaurantId,@RequestBody Restaurant restaurant) {
        logger.info("Request to update restaurant with id: {}, data: {}", restaurantId, restaurant);
        try {
            return ResponseEntity.ok(restaurantService.updateRestaurant(restaurantId, restaurant));
        } catch (RestaurantNotFoundException e) {
            logger.error("Restaurants not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid restaurant data: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Failed to update restaurant: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // To delete restaurant details
    @DeleteMapping("/restaurant/delete/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable("restaurantId") Long restaurantId) {
        logger.info("Request to delete restaurant with id: {}", restaurantId);
        try {
            restaurantService.deleteRestaurant(restaurantId);
            return ResponseEntity.noContent().build();
        } catch (RestaurantNotFoundException e) {
            logger.error("Restaurant not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Failed to delete restaurant: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
