package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.CommanUtil.ValidationClass;
import com.FoodDeliveryWebApp.Entity.Category;
import com.FoodDeliveryWebApp.Entity.Menu;
import com.FoodDeliveryWebApp.Entity.Restaurant;
import com.FoodDeliveryWebApp.Exception.RestaurantNotFoundException;
import com.FoodDeliveryWebApp.Repository.MenuRepository;
import com.FoodDeliveryWebApp.Repository.RestaurantRepository;
import com.FoodDeliveryWebApp.ServiceI.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import static com.FoodDeliveryWebApp.CommanUtil.ValidationClass.*;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public Restaurant saveRestaurants(Restaurant restaurant, List<MultipartFile> imageFiles) {
        logger.info("Saving restaurants: {}", restaurant);
        try {
            // Convert images to byte arrays and set them to the restaurant
            List<byte[]> images = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                String contentType = file.getContentType();
                if (contentType.equals("image/jpeg") || contentType.equals("image/png")) {
                    images.add(file.getBytes());
                } else {
                    throw new IllegalArgumentException("Only PNG and JPEG images are supported");
                }
            }
            restaurant.setImages(images);
            // Validate and save the restaurant
            validateRestaurantData(restaurant);
            return restaurantRepository.save(restaurant);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process image files", e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save restaurants", e);
        }
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, Restaurant updatedRestaurantData, List<MultipartFile> imageFiles) throws RestaurantNotFoundException, IOException {
        logger.info("Updating restaurant by id: {}, data: {}", restaurantId, updatedRestaurantData);

        Restaurant existingRestaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));

        // Update restaurant fields if provided
        if (updatedRestaurantData.getRestaurantName() != null) {
            existingRestaurant.setRestaurantName(updatedRestaurantData.getRestaurantName());
        }
        if (updatedRestaurantData.getRestaurantAddress() != null) {
            existingRestaurant.setRestaurantAddress(updatedRestaurantData.getRestaurantAddress());
        }
        if (updatedRestaurantData.getRestaurantContactInfo() != null) {
            existingRestaurant.setRestaurantContactInfo(updatedRestaurantData.getRestaurantContactInfo());
        }
        if (updatedRestaurantData.getCuisines() != null && !updatedRestaurantData.getCuisines().isEmpty()) {
            existingRestaurant.setCuisines(updatedRestaurantData.getCuisines());
        }
        if (updatedRestaurantData.getCategory() != null && !updatedRestaurantData.getCategory().isEmpty()) {
            existingRestaurant.setCategory(updatedRestaurantData.getCategory());
        }

        // Update images if provided
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<byte[]> images = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                String contentType = file.getContentType();
                if (contentType != null && (contentType.equalsIgnoreCase("image/jpeg") ||
                        contentType.equalsIgnoreCase("image/png") ||
                        contentType.equalsIgnoreCase("image/jpg"))) {
                    images.add(file.getBytes());
                } else {
                    throw new IllegalArgumentException("Only PNG and JPEG images are supported");
                }
            }
            existingRestaurant.setImages(images);
        }

        try {
            return restaurantRepository.save(existingRestaurant);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update restaurant", e);
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
            throw new IllegalArgumentException("Restaurant address cannot be null or empty.");
        }
        if (restaurant.getCuisines() == null || restaurant.getCuisines().isEmpty()) {
            throw new IllegalArgumentException("Cuisines list cannot be null or empty.");
        }

        for (String cuisine : restaurant.getCuisines()) {
            if (!ValidationClass.CUISINE_PATTERN.matcher(cuisine).matches()) {
                throw new IllegalArgumentException("Invalid cuisine format. Only alphabetic characters and spaces are allowed.");
            }
        }
        if (restaurant.getRestaurantContactInfo() == null || !RESTAURANT_CONTANCT_PATTERN.matcher(restaurant.getRestaurantContactInfo()).matches()) {
            throw new IllegalArgumentException("Contact number should be valid.");
        }
        if (restaurant.getCategory() == null || restaurant.getCategory().isEmpty()) {
            throw new IllegalArgumentException("Category must contain at least one value.");
        }

        for (Category category : restaurant.getCategory()) {
            boolean isValidCategory = Arrays.stream(Category.values())
                    .anyMatch(status -> status.equals(category));
            if (!isValidCategory) {
                throw new IllegalArgumentException("Category must be VEG or NON_VEG.");
            }
        }
    }

    @Override
    public List<Restaurant> findRestaurantsByMenuName(String itemName) {
        logger.info("Finding restaurants by menu item name: {}", itemName);
        try {
            List<Menu> menus = menuRepository.findByItemName(itemName);
            if (menus == null || menus.isEmpty()) {
                throw new RestaurantNotFoundException("No menus found with the name: " + itemName);
            }
            return menus.stream()
                    .map(Menu::getRestaurant)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (RestaurantNotFoundException e) {
            logger.error(e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Failed to find restaurants by menu name: {}", itemName, e);
            return Collections.emptyList();
        }
    }

    @Override
    public Restaurant getRestaurantsById(Long restaurantId) {
        logger.info("Getting restaurants by id: {}", restaurantId);
        try {
            return restaurantRepository.findById(restaurantId).orElseThrow(() ->
                    new RestaurantNotFoundException("Restaurants not found"));
        } catch(Exception | RestaurantNotFoundException e){
            throw new RuntimeException("Failed to get restaurants", e);
        }
    }

}
