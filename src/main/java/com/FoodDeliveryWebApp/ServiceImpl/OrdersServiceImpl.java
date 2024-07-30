package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.OrderStatus;
import com.FoodDeliveryWebApp.Entity.Orders;
import com.FoodDeliveryWebApp.Exception.OrdersNotFoundException;
import com.FoodDeliveryWebApp.Repository.OrdersRepository;
import com.FoodDeliveryWebApp.ServiceI.OrdersService;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.FoodDeliveryWebApp.CommanUtil.ValidationClass.PRICE_PATTERN;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    private final Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);


    @Override
    public List<Orders> getAllOrders() {
        logger.info("Getting all orders");
        try {
            return ordersRepository.findAll();
        }catch (RuntimeException e) {
            logger.warn("Error getting all orders");
            throw new OrdersNotFoundException("Failed to get all orders");
        }catch (Exception e) {
            logger.error("Error getting orders");
            throw new ObjectNotFoundException("Failed to get all orders", e);
        }
    }

    @Override
    public Orders getOrdersByUserId(Long userId) {
        logger.info("Getting orders by user id: {}", userId);
        try {
            return ordersRepository.findById(userId).orElseThrow(() -> new OrdersNotFoundException("Order not found"));
        }catch (Exception e) {
            logger.error("Error getting orders by user id: {}", userId);
            throw new OrdersNotFoundException("Failed to get orders by user id: " + userId);
        }
    }

    @Override
    public Orders getOrderByOrderId(Long orderId) {
        logger.info("Getting order by order id: {}", orderId);
        try {
            return ordersRepository.findById(orderId).orElseThrow(() ->
                    new RuntimeException("Orders not found with id: " + orderId));
        }catch (Exception e) {
            logger.error("Error getting orders by order id: {}", orderId);
            throw new RuntimeException("Failed to get order by id: " + orderId, e);
        }
    }

    @Override
    public Orders updateOrder(Long orderId, Orders orderDetails) {
        logger.info("Updating order with id: {} with new data: {}", orderId, orderDetails);
        try {
            Orders existingOrder = ordersRepository.findById(orderId)
                    .orElseThrow(() -> new OrdersNotFoundException("Order not found with id: " + orderId));

            // Update fields
            if (orderDetails.getOrderStatus() != null) {
                existingOrder.setOrderStatus(orderDetails.getOrderStatus());
            }
            if (orderDetails.getOrderDateAndTime() != null) {
                existingOrder.setOrderDateAndTime(orderDetails.getOrderDateAndTime());
            }
            if (orderDetails.getUser() != null) {
                existingOrder.setUser(orderDetails.getUser());
            }
            if (orderDetails.getRestaurant() != null) {
                existingOrder.setRestaurant(orderDetails.getRestaurant());
            }
            if (orderDetails.getOrderItems() != null) {
                existingOrder.setOrderItems(orderDetails.getOrderItems());
            }

           // validateOrders(existingOrder);
            return ordersRepository.save(existingOrder);
        } catch (OrdersNotFoundException e) {
            logger.warn("Order not found with id: {}", orderId);
            throw e;
        } catch (Exception e) {
            logger.error("Error updating order with id: {}", orderId, e);
            throw new RuntimeException("Failed to update order with id: " + orderId, e);
        }
    }


    @Override
    public Orders cancelOrder(Long orderId) {
        logger.info("Cancelling order: {}", orderId);
        try {
            Orders order = ordersRepository.findById(orderId).orElseThrow(() ->
                    new OrdersNotFoundException("Order not found with id: " + orderId));
            order.setOrderStatus(OrderStatus.CANCELLED);
            return ordersRepository.save(order);
        } catch (OrdersNotFoundException e) {
            logger.warn("Order not found with given orderId");
            throw e;
        } catch (Exception e) {
            logger.error("Error cancelling order by order id: {}", orderId);
            throw new RuntimeException("Failed to cancel order: " + orderId, e);
        }
    }

//    private void validateOrders(Orders orders) {
//        // Add validation logic here
//        if (orders.getTotalPrice() == null || orders.getTotalPrice() <= 0) {
//            throw new IllegalArgumentException("Total price must be greater than 0");
//        }
//        if (orders.getUserId() == null) {
//            throw new IllegalArgumentException("User id is required");
//        }
//
//        if (orders.getRestaurantId() == null) {
//            throw new IllegalArgumentException("Restaurant id is required");
//        }
//
//        if (!PRICE_PATTERN.matcher(String.valueOf(orders.getTotalPrice())).matches()) {
//            throw new IllegalArgumentException("Total price must be a valid numeric value");
//        }
//
//        if (orders.getOrderDate() == null) {
//            throw new IllegalArgumentException("Order date is required");
//        }
//
//        if (orders.getOrderStatus() == null) {
//            throw new IllegalArgumentException("Order status must be PLACED, PREPARING, DELIVERING, DELIVERED, or CANCELLED");
//        }
//
//        boolean isValidStatus = Arrays.stream(OrderStatus.values())
//                .anyMatch(status -> status.equals(orders.getOrderStatus()));
//
//        if (!isValidStatus) {
//            throw new IllegalArgumentException("Order status must be PLACED, PREPARING, DELIVERING, DELIVERED, or CANCELLED.");
//        }
//
//    }
}
