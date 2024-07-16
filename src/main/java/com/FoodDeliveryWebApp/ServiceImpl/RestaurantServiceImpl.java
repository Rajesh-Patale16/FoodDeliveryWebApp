package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.CommanUtil.ValidationClass;
import com.FoodDeliveryWebApp.Entity.Restaurant;
import com.FoodDeliveryWebApp.Exception.RestaurantNotFoundException;
import com.FoodDeliveryWebApp.Repository.RestaurantRepository;
import com.FoodDeliveryWebApp.ServiceI.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.FoodDeliveryWebApp.CommanUtil.ValidationClass.*;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Restaurant saveRestaurants(Restaurant restaurant) {
        logger.info("Saving restaurants: {}", restaurant);
        try {
            validateRestaurantData(restaurant);
            return restaurantRepository.save(restaurant);
        } catch(IllegalArgumentException e){
            throw e;
        } catch(Exception e){
            throw new RuntimeException("Failed to save restaurants", e);
        }
    }

    @Override
    public Restaurant getRestaurantsByName(String restaurantName) throws RestaurantNotFoundException {


        if (restaurantName == null || restaurantName.isEmpty()) {
            throw new IllegalArgumentException("Restaurant name cannot be null or empty");
        }
        logger.info("Getting restaurant by name: {}", restaurantName);
        try {
            return restaurantRepository.findByRestaurantName(restaurantName)
                    .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with name: " + restaurantName));
        } catch (DataAccessException e) {
            logger.error("Failed to get restaurant by name: {}", restaurantName, e);
            throw new RuntimeException("Failed to get restaurant by name: " + restaurantName, e);
        }
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        logger.info("Getting all restaurants");

        try {
            return restaurantRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Failed to get all restaurants: {}", e.getMessage());
            throw new RuntimeException("Failed to get all restaurants", e);
        }
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, Restaurant restaurant) throws RestaurantNotFoundException {
        if (restaurantId == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        logger.info("Updating restaurant by id: {}, data: {}", restaurantId, restaurant);
        try {
            Restaurant existingRestaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
            // Update fields
            if (restaurant.getRestaurantName() != null) {
                existingRestaurant.setRestaurantName(restaurant.getRestaurantName());
            }
            if (restaurant.getRestaurantAddress() != null) {
                existingRestaurant.setRestaurantAddress(restaurant.getRestaurantAddress());
            }
            if (restaurant.getCuisines() != null) {
                existingRestaurant.setCuisines(restaurant.getCuisines());
            }
            if (restaurant.getRating() != null) {
                existingRestaurant.setRating(restaurant.getRating());
            }
            // Validate updated data
            validateRestaurantData(existingRestaurant);
            // Save and return updated restaurant
            return restaurantRepository.save(existingRestaurant);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Failed to update restaurant due to data integrity violation", e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to update restaurant with id: " + restaurantId, e);
        }
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws RestaurantNotFoundException {

        if (restaurantId == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        logger.info("Deleting restaurant by id: {}", restaurantId);
        try {
            Restaurant existingRestaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
            restaurantRepository.deleteById(restaurantId);
            logger.info("Successfully deleted restaurant with id: {}", restaurantId);
        } catch (EmptyResultDataAccessException e) {
            throw new RestaurantNotFoundException("Restaurant not found"); // Handles case where findById doesn't throw
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to delete restaurant with id: " + restaurantId, e); // Handles general database access errors
        }

    }


    private void validateRestaurantData(Restaurant restaurant) {
        if (restaurant.getRestaurantName() == null || !NAME_PATTERN.matcher(restaurant.getRestaurantName()).matches()) {
            throw new IllegalArgumentException("Invalid restaurant name format. Only alphanumeric characters, underscores, dots, @, -, spaces, and parentheses are allowed.");
        }
        if (restaurant.getRestaurantAddress() == null || !ADDRESS_PATTERN.matcher(restaurant.getRestaurantAddress()).matches()) {
            throw new IllegalArgumentException("Invalid address format. Only alphanumeric characters, spaces, commas, periods, apostrophes, and hyphens are allowed.");
        }
        if (restaurant.getCuisines() == null || restaurant.getCuisines().isEmpty()) {
            throw new IllegalArgumentException("Cuisines list cannot be null or empty.");
        }

        for (String cuisine : restaurant.getCuisines()) {
            if (!ValidationClass.CUISINE_PATTERN.matcher(cuisine).matches()) {
                throw new IllegalArgumentException("Invalid cuisine format. Only alphabetic characters and spaces are allowed.");
            }
        }
        if (restaurant.getRating() == null) {
            throw new IllegalArgumentException("Rating cannot be null.");
        } else {
            String ratingString = restaurant.getRating().toString();
            try {
                if (!RATING_PATTERN.matcher(ratingString).matches()) {
                    throw new IllegalArgumentException("Invalid rating format. Only numeric characters and a decimal point are allowed.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid rating format. Rating must be a valid numeric value.");
            }
        }
    }
}
