package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.Menu;
import com.FoodDeliveryWebApp.Exception.MenuNotFoundException;
import com.FoodDeliveryWebApp.Exception.RestaurantNotFoundException;
import com.FoodDeliveryWebApp.Repository.MenuRepository;
import com.FoodDeliveryWebApp.Repository.RestaurantRepository;
import com.FoodDeliveryWebApp.ServiceI.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import static com.FoodDeliveryWebApp.CommanUtil.ValidationClass.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    private static final String UPLOADED_FOLDER = "src/main/resources/static/images/";

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    public Menu saveMenu(Menu menu) {
        logger.info("Saving menu: {}", menu);
        try{
            validationMenuData(menu);
            return menuRepository.save(menu);
        }catch (IllegalArgumentException e){
            logger.error("Error saving menu");
            throw e;
        }catch (Exception e){
            logger.error("Failed to save menu: {}", e.getMessage());
            throw new RuntimeException("Failed to save menu", e);
        }
    }

    public List<Menu> getAllMenus() {
        logger.info("Getting all menus");
        try{
            return menuRepository.findAll();
        }catch (Exception e){
            logger.error("Failed to get all menus: {}", e.getMessage());
            throw new RuntimeException("Failed to get all menus", e);
        }
    }

    @Override
    public List<Menu> getItemNamesByRestaurantName(String restaurantName) throws RestaurantNotFoundException {
        logger.info("Fetching menu items for restaurant name: {}", restaurantName);
        List<Menu> menus = menuRepository.findByRestaurantName(restaurantName);
        if (menus.isEmpty()) {
            logger.error("No menus found for restaurant name: {}", restaurantName);
            throw new MenuNotFoundException("No menu items found for restaurant name: " + restaurantName);
        }
        return menus.stream().map(menu->new Menu(menu.getItemName(),menu.getDescription(),menu.getPrice())).collect(Collectors.toList());
    }

    @Override
    public Menu updateMenuByRestaurantIdAndItemName(Long restaurantId, String itemName, Menu menu) {
        logger.info("Updating menu with item name: {} for restaurant id: {}", itemName, restaurantId);
        Menu existingMenu = menuRepository.findByRestaurantIdAndItemName(restaurantId, itemName)
                .orElseThrow(() -> new MenuNotFoundException("Menu not found with name: " + itemName + " for restaurant id: " + restaurantId));

        existingMenu.setItemName(menu.getItemName());
        existingMenu.setDescription(menu.getDescription());
        existingMenu.setPrice(menu.getPrice());

        Menu savedMenu = menuRepository.save(existingMenu);
        logger.info("Updated menu: {}", savedMenu);
        return savedMenu;
    }

    @Override
    public Menu getMenuById(Long menuId) {
        return menuRepository.findById(menuId).
                orElseThrow(() -> new MenuNotFoundException("Menu not found"));
    }

    @Override
    public Menu updateMenu(Long menuId, Menu menu) throws MenuNotFoundException {
        if (menuId == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        try {
            Menu existingMenu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new MenuNotFoundException("Restaurant not found"));
            // Update fields
            if (menu.getProfilePicture() != null) {
                existingMenu.setProfilePicture(menu.getProfilePicture());
            }
            // Save and return updated menu
            return menuRepository.save(existingMenu);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Failed to update restaurant due to data integrity violation", e);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to update restaurant with id: " + menuId, e);
        }
    }

    @Override
    public void deleteMenuByIdAndName(Long menuId) {
        logger.info("Deleting menu id with item name: {}", menuId);
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("Menu not found with id: " + menuId));

        if (!menu.getMenuId().equals(menuId)) {
            logger.error("Menu name does not match for id: {}", menuId);
            throw new MenuNotFoundException("Menu name does not match for id: " + menuId);
        }

        menuRepository.delete(menu);
        logger.info("Deleted menu id with item name: {}", menuId);
    }

    public void validationMenuData(Menu menu){
        if (menu.getItemName() == null || !MENU_ITEM_PATTERN.matcher(menu.getItemName()).matches()){
            throw new IllegalArgumentException("Invalid item name. Item name should be alphanumeric and at least 5 characters long");
        }
        if (menu.getPrice() == null || !isValidPrice(menu.getPrice())) {
            throw new IllegalArgumentException("Invalid price. Price should be a valid decimal number with 2 decimal places");
        }
        if(menu.getDescription() == null || !DESCRIPTION_PATTERN.matcher(menu.getDescription()).matches()){
            throw new IllegalArgumentException("Invalid description. Description should be alphanumeric and at least 10 characters long");
        }
        if(menu.getRestaurant() == null || menu.getRestaurant().getRestaurantId() == null){
            throw new IllegalArgumentException("Restaurant id cannot be null");
        }
    }

    private boolean isValidPrice(double price) {
        // Check if the price is a non-negative number
        if (price < 0) {
            return false;
        }
        // Check if the price has more than 2 decimal places
        return Math.floor(price * 100) == price * 100;
    }

}

