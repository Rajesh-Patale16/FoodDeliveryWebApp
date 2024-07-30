package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.Menu;
import com.FoodDeliveryWebApp.Entity.Restaurant;
import com.FoodDeliveryWebApp.Exception.MenuNotFoundException;
import com.FoodDeliveryWebApp.Exception.RestaurantNotFoundException;

import java.util.List;

public interface MenuService {

    Menu saveMenu(Menu menu);

    List<Menu> getAllMenus();

    void deleteMenuByIdAndName(Long menuId);

    Menu updateMenuByRestaurantIdAndItemName(Long restaurantId, String itemName, Menu menu);

    List<Menu> getItemNamesByRestaurantName(String restaurantName) throws RestaurantNotFoundException;

    Menu getMenuById(Long menuId);

    Menu updateMenu(Long menuId, Menu menu) throws MenuNotFoundException;
}
