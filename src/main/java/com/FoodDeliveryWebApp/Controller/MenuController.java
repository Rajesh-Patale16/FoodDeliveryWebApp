package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.Menu;
import com.FoodDeliveryWebApp.Entity.Restaurant;
import com.FoodDeliveryWebApp.Exception.MenuNotFoundException;
import com.FoodDeliveryWebApp.Exception.RestaurantNotFoundException;
import com.FoodDeliveryWebApp.ServiceI.MenuService;
import com.FoodDeliveryWebApp.ServiceI.RestaurantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin("*")
public class MenuController {

    private final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping(value = "/menu/save/{restaurantId}", consumes = "multipart/form-data")
    public ResponseEntity<Menu> addMenu(
            @PathVariable Long restaurantId,
            @RequestPart("menu") Menu menu,
            @RequestPart("images") List<MultipartFile> imageFiles) {
        logger.info("Request to save menu: {}", menu);
        try {
            Restaurant restaurant = restaurantService.getRestaurantsById(restaurantId);
            if (restaurant == null) {
                logger.warn("Restaurant not find");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            menu.setRestaurant(restaurant);
            Menu savedMenu = menuService.saveMenu(menu, imageFiles);
            return ResponseEntity.ok(savedMenu);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid menu data: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Failed to save menu: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (RestaurantNotFoundException e) {
            throw new RuntimeException(e);
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

    @PutMapping(value = "/menu/update/{restaurantId}/{itemName}", consumes = "multipart/form-data")
    public ResponseEntity<Menu> updateMenuByRestaurantIdAndItemName(
            @PathVariable Long restaurantId,
            @PathVariable String itemName,
            @RequestPart("menu") Menu updatedMenuData,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles) {
        logger.info("Request to update menu for restaurantId: {}, itemName: {}", restaurantId, itemName);
        try {
            Menu updatedMenu = menuService.updateMenuByRestaurantIdAndItemName(restaurantId, itemName, updatedMenuData, imageFiles);
            return ResponseEntity.ok(updatedMenu);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid menu data : {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Failed to update menu: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("/menu/delete/{menuId}")
    public ResponseEntity<String> deleteMenuByIdAndName(@PathVariable Long menuId) {
        logger.info("Received request to delete menu id: {} with item name", menuId);
        try{
            menuService.deleteMenuByIdAndName(menuId);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        }catch (MenuNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu not found for given menu id: {}"+menuId);
        }

    }

    @GetMapping("/find/{menuId}")
    public Menu getMenu(@PathVariable Long menuId){
        return menuService.getMenuById(menuId);
    }
}