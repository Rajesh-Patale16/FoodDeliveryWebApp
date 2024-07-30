package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.Menu;
import com.FoodDeliveryWebApp.Entity.OrderItem;
import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.ServiceI.MenuService;
import com.FoodDeliveryWebApp.ServiceI.OrderItemService;
import com.FoodDeliveryWebApp.ServiceI.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orderItems")
@CrossOrigin("*")
public class OrderItemController {

    private final Logger logger = LoggerFactory.getLogger(OrderItemController.class);

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @PostMapping("/order/save/{userId}/{menuId}")
    public ResponseEntity<?> saveOrder(@RequestBody OrderItem orderItem,
                                       @PathVariable Long userId,
                                       @PathVariable Long menuId) {
        try {
            logger.info("Saving order item: {}", orderItem);

            // Fetch user and menu
            User user = userService.getUserById(userId);
            if (user == null) {
                logger.warn("User with ID {} not found", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            Menu menu = menuService.getMenuById(menuId);
            if (menu == null) {
                logger.warn("Menu with ID {} not found", menuId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu not found");
            }

            // Set user and menu to order item
            orderItem.setMenu(menu);
            orderItem.setUser(user);

            // Save order item
            OrderItem savedOrderItem = orderItemService.saveOrderItem(orderItem);
            logger.info("Order item saved successfully: {}", savedOrderItem);
            return ResponseEntity.ok(savedOrderItem);
        } catch (Exception e) {
            logger.error("Error while saving order item: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving order item");
        }
    }

}

