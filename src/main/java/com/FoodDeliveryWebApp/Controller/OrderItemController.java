package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.Menu;
import com.FoodDeliveryWebApp.Entity.OrderItem;
import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.ServiceI.MenuService;
import com.FoodDeliveryWebApp.ServiceI.OrderItemService;
import com.FoodDeliveryWebApp.ServiceI.UserService;
import com.FoodDeliveryWebApp.Exception.OrderItemNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/order/update/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderItem orderItem) {
        try {
            logger.info("Updating order item with ID: {}", id);
            OrderItem updatedOrderItem = orderItemService.updateOrderItem(id, orderItem);
            return ResponseEntity.ok(updatedOrderItem);
        } catch (OrderItemNotFoundException e) {
            logger.warn("Order item with ID {} not found for update", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error while updating order item with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating order item");
        }
    }

    @DeleteMapping("/order/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            logger.info("Deleting order item with ID: {}", id);
            orderItemService.deleteOrderItem(id);
            return ResponseEntity.ok("Order item deleted successfully");
        } catch (OrderItemNotFoundException e) {
            logger.warn("Order item with ID {} not found for deletion", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error while deleting order item with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting order item");
        }
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            logger.info("Fetching order item with ID: {}", id);
            OrderItem orderItem = orderItemService.getOrderItemById(id);
            return ResponseEntity.ok(orderItem);
        } catch (OrderItemNotFoundException e) {
            logger.warn("Order item with ID {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error while fetching order item with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching order item");
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        try {
            logger.info("Fetching all order items");
            List<OrderItem> orderItems = orderItemService.getAllOrderItems();
            return ResponseEntity.ok(orderItems);
        } catch (Exception e) {
            logger.error("Error while fetching all order items: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching all order items");
        }
    }
}
