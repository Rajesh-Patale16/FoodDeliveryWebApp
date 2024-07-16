package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.Restaurant;
import com.FoodDeliveryWebApp.Exception.RestaurantNotFoundException;
import java.util.List;

public interface RestaurantService {

    Restaurant saveRestaurants(Restaurant restaurant);

    Restaurant getRestaurantsByName(String restaurantName) throws RestaurantNotFoundException;

    List<Restaurant> getAllRestaurants();

    Restaurant updateRestaurant(Long restaurantId, Restaurant restaurant) throws RestaurantNotFoundException;

    void deleteRestaurant(Long restaurantId) throws RestaurantNotFoundException;

    Restaurant getRestaurantsById(Long restaurantId) throws RestaurantNotFoundException;

    List<Restaurant> findRestaurantsByMenuName(String itemName);
}
