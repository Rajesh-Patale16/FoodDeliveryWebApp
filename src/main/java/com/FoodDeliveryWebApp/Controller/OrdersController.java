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
@CrossOrigin("*")  // Enable Cross-Origin Resource Sharing (CORS) for all endpoints in this controller. Replace "*" with appropriate origins if necessary.
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
    public ResponseEntity<List<Orders>> getAllOrders(){
        try {
            return ResponseEntity.ok().body(ordersService.getAllOrders());
        } catch (Exception e) {
            logger.error("Error while getting orders");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/order/byUserId/{userId}")
    public ResponseEntity<Orders> getOrdersByUserId(@PathVariable("userId") Long userId){
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User id cannot be null");
            }
            return ResponseEntity.ok().body(ordersService.getOrdersByUserId(userId));
        } catch (Exception e) {
            logger.error("Error finding user: {}", userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/order/byOrderId/{orderId}")
    public ResponseEntity<Orders> getOrderByOrderId(@PathVariable("orderId") Long orderId){
        try {
            if (orderId == null) {
                throw new IllegalArgumentException("Order id cannot be null");
            }
            return ResponseEntity.ok().body(ordersService.getOrderByOrderId(orderId));
        } catch (Exception e) {
            logger.error("Error finding order: {}", orderId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/order/update/{orderId}")
    public ResponseEntity<Orders> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody Orders orders){
        try {
            if (orderId == null) {
                throw new IllegalArgumentException("Order id cannot be null");
            }
            return ResponseEntity.ok().body(ordersService.updateOrder(orderId, orders));
        } catch (OrdersNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error updating order: {}", orderId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //cancelOrder
    @PostMapping("/order/cancel/{orderId}")
    public ResponseEntity<Orders> cancelOrder(@PathVariable("orderId") Long orderId){
        try {
            if (orderId == null) {
                throw new IllegalArgumentException("Order id cannot be null");
            }
            Orders cancelledOrder = ordersService.cancelOrder(orderId);
            return ResponseEntity.ok(cancelledOrder);
        } catch (OrdersNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error cancelling order: {}", orderId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
