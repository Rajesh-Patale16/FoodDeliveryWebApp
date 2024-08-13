package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.Restaurant;
import com.FoodDeliveryWebApp.Exception.RestaurantNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RestaurantService {

    Restaurant saveRestaurants(Restaurant restaurant, List<MultipartFile> imageFiles);

    Restaurant updateRestaurant(Long restaurantId, Restaurant restaurant, List<MultipartFile> images) throws RestaurantNotFoundException, IOException;

    Restaurant getRestaurantsByName(String restaurantName) throws RestaurantNotFoundException;

    List<Restaurant> getAllRestaurants();

    void deleteRestaurant(Long restaurantId) throws RestaurantNotFoundException;

    Restaurant getRestaurantsById(Long restaurantId) throws RestaurantNotFoundException;

    List<Restaurant> findRestaurantsByMenuName(String itemName);
}
