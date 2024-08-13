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
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin("*")
public class RestaurantController {

    private final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    private RestaurantService restaurantService;

    // Save restaurant with images
    @PostMapping("/restaurant/save")
    public ResponseEntity<Restaurant> addRestaurant(
            @RequestPart("restaurant") Restaurant restaurant,
            @RequestPart("images") List<MultipartFile> imageFiles) {
        logger.info("Request to save restaurant: {}", restaurant);
        try {
            return ResponseEntity.ok(restaurantService.saveRestaurants(restaurant, imageFiles));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid restaurant data : {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Failed to save restaurant: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Update restaurant with images
    @PutMapping(value = "/restaurant/update/{restaurantId}", consumes = "multipart/form-data")
    public ResponseEntity<Restaurant> updateRestaurant(
            @PathVariable("restaurantId") Long restaurantId,
            @RequestPart("restaurant") Restaurant restaurant,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        logger.info("Request to update restaurant with id: {}, data: {}", restaurantId, restaurant);
        try {

            return ResponseEntity.ok(restaurantService.updateRestaurant(restaurantId, restaurant, images));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid restaurant data: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (RestaurantNotFoundException e) {
            logger.error("Restaurant not found : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Failed to update restaurant: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get restaurant by name
    @GetMapping("/restaurant/find/{restaurantName}")
    public ResponseEntity<Restaurant> getRestaurantByName(@PathVariable("restaurantName") String restaurantName) {
        logger.info("Request to get restaurant by name: {}", restaurantName);
        try {
            return ResponseEntity.ok(restaurantService.getRestaurantsByName(restaurantName));
        } catch (RestaurantNotFoundException e) {
            logger.error("Restaurant not found :- {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Failed to get restaurant : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get all restaurants
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

    // Delete restaurant by ID
    @DeleteMapping("/restaurant/delete/{restaurantId}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable("restaurantId") Long restaurantId) {
        logger.info("Request to delete restaurant with id: {}", restaurantId);
        try {
            restaurantService.deleteRestaurant(restaurantId);
            return ResponseEntity.status(HttpStatus.OK).body("Restaurant deleted successfully.");
        } catch (RestaurantNotFoundException e) {
            logger.error("Restaurant not found - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found.");
        } catch (Exception e) {
            logger.error("Failed to delete restaurant: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete.");
        }
    }

    // Find restaurants by menu item name
    @GetMapping("/restaurant/findByMenuItem/{itemName}")
    public ResponseEntity<List<Restaurant>> findRestaurantsByMenuName(@PathVariable("itemName") String itemName) {
        logger.info("Request to find restaurants by menu item name: {}", itemName);
        try {
            return ResponseEntity.ok(restaurantService.findRestaurantsByMenuName(itemName));
        } catch (Exception e) {
            logger.error("Failed to find restaurants: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get restaurant by ID
    @GetMapping("/restaurant/findById/{restaurantId}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable("restaurantId") Long restaurantId) {
        logger.info("Request to get restaurant by id: {}", restaurantId);
        try {
            return ResponseEntity.ok(restaurantService.getRestaurantsById(restaurantId));
        } catch (RestaurantNotFoundException e) {
            logger.error("Restaurant not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Failed to get restaurant: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
