package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.OrderItem;

import java.util.List;
import java.util.Map;

public interface OrderItemService {
    OrderItem saveOrderItem(OrderItem orderItem);

    OrderItem updateOrderItem(Long id, OrderItem orderItem);

    void deleteOrderItem(Long id);

//    OrderItem getOrderItemById(Long id);
//
//    List<OrderItem> getAllOrderItems();

    Map<String, Object> getOrderItemById(Long id);

    List<Map<String, Object>> getAllOrderItems();
}
