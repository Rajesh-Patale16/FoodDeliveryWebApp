package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.OrderStatus;
import com.FoodDeliveryWebApp.Entity.Orders;
import com.FoodDeliveryWebApp.Exception.OrdersNotFoundException;
import com.FoodDeliveryWebApp.Repository.OrdersRepository;
import com.FoodDeliveryWebApp.ServiceI.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    private final Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);

    @Override
    public List<Orders> getAllOrders() {
        logger.info("Getting all orders");
        return ordersRepository.findAll();
    }

    @Override
    public List<Orders> getOrdersByUserId(Long userId) {
        logger.info("Getting orders by user id: {}", userId);
        return ordersRepository.findByUserId(userId);
    }

    @Override
    public Orders getOrderByOrderId(Long orderId) {
        logger.info("Getting order by order id: {}", orderId);
        return ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrdersNotFoundException("Orders not found with id: " + orderId));
    }

    @Override
    public Orders updateOrder(Long orderId, Orders orderDetails) {
        logger.info("Updating order with id: {} with new data: {}", orderId, orderDetails);
        Orders existingOrder = ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrdersNotFoundException("Order not found with id: " + orderId));

        // Update fields
        if (orderDetails.getOrderStatus() != null) {
            existingOrder.setOrderStatus(orderDetails.getOrderStatus());
        }
        if (orderDetails.getOrderDateAndTime() != null) {
            existingOrder.setOrderDateAndTime(orderDetails.getOrderDateAndTime());
        }

        return ordersRepository.save(existingOrder);
    }

    @Override
    public Orders cancelOrder(Long orderId, Long userId) {
        logger.info("Cancelling order with id: {}", orderId);
        Orders existingOrder = ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrdersNotFoundException("Order not found with id: " + orderId));

        if (existingOrder.getOrderStatus() != OrderStatus.PLACED) {
            throw new IllegalStateException("Order cannot be cancelled as it is not in PLACED status");
        }

        LocalDateTime orderDateAndTime = existingOrder.getOrderDateAndTime();
        LocalDateTime currentTime = LocalDateTime.now();
        Duration timeElapsed = Duration.between(orderDateAndTime, currentTime);

        if (timeElapsed.toMinutes() >= 2) {
            throw new IllegalStateException("Order cannot be cancelled as more than 2 minute.");
        }
        existingOrder.setOrderStatus(OrderStatus.CANCELLED);
        return ordersRepository.save(existingOrder);
    }

    @Override
    public Orders saveOrder(Orders orders) {
        logger.info("Saving order: {}", orders);
        return ordersRepository.save(orders);
    }
}