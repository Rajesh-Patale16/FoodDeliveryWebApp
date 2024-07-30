package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.Menu;
import com.FoodDeliveryWebApp.Entity.Restaurant;
import com.FoodDeliveryWebApp.Exception.MenuNotFoundException;
import com.FoodDeliveryWebApp.Exception.RestaurantNotFoundException;
import com.FoodDeliveryWebApp.ServiceI.MenuService;
import com.FoodDeliveryWebApp.ServiceI.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin("*")
public class MenuController {

    private final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/menu/save/{restaurantId}")
    public ResponseEntity<Menu> saveMenu(@PathVariable Long restaurantId, @RequestBody Menu menu) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantsById(restaurantId);
            if (restaurant == null) {
                logger.warn("Restaurant not find");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            menu.setRestaurant(restaurant);
            Menu save = menuService.saveMenu(menu);
            return ResponseEntity.ok(save);
        } catch (Exception | RestaurantNotFoundException e) {
            logger.error("error saving menu", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @GetMapping("/menu/getAllMenus")
    public ResponseEntity<List<Menu>> getAllMenus() {
        return ResponseEntity.ok(menuService.getAllMenus());
    }

    @GetMapping("/menu/by-menu/{itemName}")
    public ResponseEntity<?> getRestaurantsByMenuName(@PathVariable("itemName") String itemName) {
        try {
            List<Restaurant> restaurants = restaurantService.findRestaurantsByMenuName(itemName);
            if (restaurants.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No restaurants found for the menu name: " + itemName);
            } else {
                return ResponseEntity.ok(restaurants);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

//    To get a restaurant name by item name
    @GetMapping("/menu/items-by-restaurant/{restaurantName}")
    public ResponseEntity<List<Menu>> getItemNamesByRestaurantName(@PathVariable String restaurantName) throws RestaurantNotFoundException {
        logger.info("Received request to fetch item names for restaurant name: {}", restaurantName);

            List<Menu> itemNames = menuService.getItemNamesByRestaurantName(restaurantName);
            return ResponseEntity.ok(itemNames);

    }

    @PutMapping("/menu/update/{restaurantId}/{itemName}")
    public ResponseEntity<?> updateMenuByRestaurantIdAndItemName(
            @PathVariable Long restaurantId, @PathVariable String itemName,  @RequestBody Menu menu) {
        logger.info("Received request to update menu with item name: {} for restaurant id: {}", itemName, restaurantId);
        try{
            Menu updatedMenu = menuService.updateMenuByRestaurantIdAndItemName(restaurantId, itemName, menu);
            return ResponseEntity.ok(updatedMenu);
        }catch (MenuNotFoundException e) {
            logger.warn("Could not update item with given item name: {} for restaurant id: {}"+ itemName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body("Menu not found for given restaurant id: " + restaurantId + " and item name: " + itemName);
        }
    }

    @DeleteMapping("/menu/delete/{menuId}")
    public ResponseEntity<String> deleteMenuByIdAndName(@PathVariable Long menuId) {
        logger.info("Received request to delete menu id: {} with item name: {}", menuId);
        try{
            menuService.deleteMenuByIdAndName(menuId);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }catch (MenuNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu not found for given menu id: {}"+menuId);
        }
    }

}
