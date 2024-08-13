package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.OrderItem;

import java.util.List;

public interface OrderItemService {
    OrderItem saveOrderItem(OrderItem orderItem);

    OrderItem updateOrderItem(Long id, OrderItem orderItem);

    void deleteOrderItem(Long id);

    OrderItem getOrderItemById(Long id);

    List<OrderItem> getAllOrderItems();
}
