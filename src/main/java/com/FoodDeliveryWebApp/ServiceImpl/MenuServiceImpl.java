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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Override
    public Menu saveMenu(Menu menu, List<MultipartFile> imageFiles) {
        logger.info("Saving menu: {}", menu);

        try {
            // Convert images to byte arrays and set them to the menu
            List<byte[]> images = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                String contentType = file.getContentType();
                if (contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                    images.add(file.getBytes());
                } else {
                    throw new IllegalArgumentException("Only PNG and JPEG images are supported");
                }
            }
            menu.setImages(images);

            // Validate and save the menu
            validationMenuData(Optional.of(menu));
            return menuRepository.save(menu);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process image files", e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
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
    public Menu updateMenuByRestaurantIdAndItemName(Long restaurantId, String itemName, Menu updatedMenuData, List<MultipartFile> imageFiles) throws IOException {
        logger.info("Updating menu for restaurantId: {}, itemName: {}", restaurantId, itemName);

        Optional<Menu> existingMenuOptional = menuRepository.findByRestaurantIdAndItemName(restaurantId, itemName);

        if (existingMenuOptional.isEmpty()) {
            throw new IllegalArgumentException("Menu item not found for the given restaurant and item name");
        }
        Menu existingMenu = existingMenuOptional.get();
        // Update the menu details
        existingMenu.setItemName(updatedMenuData.getItemName());
        existingMenu.setPrice(updatedMenuData.getPrice());
        existingMenu.setDescription(updatedMenuData.getDescription());
        // Update images if provided
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<byte[]> images = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                String contentType = file.getContentType();
                if (contentType != null && (contentType.equalsIgnoreCase("image/jpeg") || contentType.equalsIgnoreCase("image/png") || contentType.equalsIgnoreCase("image/jpg"))) {
                    images.add(file.getBytes());
                } else {
                    throw new IllegalArgumentException("Only PNG and JPEG images are supported");
                }
            }
            existingMenu.setImages(images);
        }
        // Validate and save the updated menu
        validationMenuData(Optional.of(existingMenu));
        try {
            return menuRepository.save(existingMenu);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update menu", e);
        }
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

    public void validationMenuData(Optional<Menu> menuOptional) {
        if (menuOptional.isEmpty()) {
            throw new IllegalArgumentException("Menu data cannot be empty");
        }

        Menu menu = menuOptional.get();

        if (menu.getItemName() == null || menu.getItemName().isEmpty() || !MENU_ITEM_PATTERN.matcher(menu.getItemName()).matches()) {
            throw new IllegalArgumentException("Invalid item name. Item name should be alphanumeric and at least 5 characters long");
        }

        if (menu.getPrice() == null || !isValidPrice(menu.getPrice())) {
            throw new IllegalArgumentException("Invalid price. Price should be a valid decimal number with 2 decimal places");
        }

        if (menu.getDescription() == null || menu.getDescription().isEmpty() || !DESCRIPTION_PATTERN.matcher(menu.getDescription()).matches()) {
            throw new IllegalArgumentException("Invalid description. Description should be alphanumeric and at least 10 characters long");
        }

        if (menu.getRestaurant() == null || menu.getRestaurant().getRestaurantId() == null) {
            throw new IllegalArgumentException("Restaurant ID cannot be null");
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

