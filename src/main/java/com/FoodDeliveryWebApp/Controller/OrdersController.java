package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.Orders;
import com.FoodDeliveryWebApp.Exception.OrdersNotFoundException;
import com.FoodDeliveryWebApp.ServiceI.MenuService;
import com.FoodDeliveryWebApp.ServiceI.OrdersService;
import com.FoodDeliveryWebApp.ServiceI.RestaurantService;
import com.FoodDeliveryWebApp.ServiceI.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    private final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @GetMapping("/order/getAllOrders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        try {
            return ResponseEntity.ok().body(ordersService.getAllOrders());
        } catch (Exception e) {
            logger.error("Error while getting orders", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/order/byUserId/{userId}")
    public ResponseEntity<List<Orders>> getOrdersByUserId(@PathVariable("userId") Long userId) {
        try {
            return ResponseEntity.ok().body(ordersService.getOrdersByUserId(userId));
        } catch (Exception e) {
            logger.error("Error finding orders for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/order/byOrderId/{orderId}")
    public ResponseEntity<Orders> getOrderByOrderId(@PathVariable("orderId") Long orderId) {
        try {
            return ResponseEntity.ok().body(ordersService.getOrderByOrderId(orderId));
        } catch (Exception e) {
            logger.error("Error finding order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/order/update/{orderId}")
    public ResponseEntity<Orders> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody Orders orders) {
        try {
            return ResponseEntity.ok().body(ordersService.updateOrder(orderId, orders));
        } catch (OrdersNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/order/cancel/{orderId}/{userId}")
    public ResponseEntity<Orders> cancelOrder(@PathVariable("orderId") Long orderId, @PathVariable("userId") Long userId) {
        try {
            Orders cancelledOrder = ordersService.cancelOrder(orderId, userId);
            return ResponseEntity.ok(cancelledOrder);
        } catch (OrdersNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Error cancelling order: {}", orderId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @PostMapping("/order/save")
    public ResponseEntity<Orders> saveOrder(@RequestBody Orders orders) {
        try {
            Orders savedOrder = ordersService.saveOrder(orders);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (Exception e) {
            logger.error("Error saving order: {}", orders, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}