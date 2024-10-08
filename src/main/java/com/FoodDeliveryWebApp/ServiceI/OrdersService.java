package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.Orders;
import java.util.List;

public interface OrdersService {
    List<Orders> getAllOrders();
    List<Orders> getOrdersByUserId(Long userId);
    Orders getOrderByOrderId(Long orderId);
    Orders updateOrder(Long orderId, Orders orders);
    Orders cancelOrder(Long orderId, Long userId);
    Orders saveOrder(Orders order);

}