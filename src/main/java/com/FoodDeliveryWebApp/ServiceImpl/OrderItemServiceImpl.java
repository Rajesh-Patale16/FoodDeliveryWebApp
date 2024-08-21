package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.OrderItem;
import com.FoodDeliveryWebApp.Entity.Orders;
import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.Exception.OrderItemNotFoundException;
import com.FoodDeliveryWebApp.Repository.OrderItemRepository;
import com.FoodDeliveryWebApp.ServiceI.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem updateOrderItem(Long id, OrderItem orderItem) {

        User user = new User();
        if (!orderItemRepository.existsById(id)) {
            throw new OrderItemNotFoundException("Order item not found");
        }
        OrderItem existingOrderItem=orderItemRepository.findById(id).orElseThrow(() ->
                new OrderItemNotFoundException("order item not found"));
        orderItem.setId(id);
        orderItem.setUser(existingOrderItem.getUser());
        orderItem.setQuantity(orderItem.getQuantity());
        orderItem.setPrice(orderItem.getPrice());
        orderItem.setTotalPrice(orderItem.getTotalPrice());
        orderItem.setGst(orderItem.getGst());
        orderItem.setGrandTotalPrice(orderItem.getGrandTotalPrice());
        orderItem.setDeliveryCharge(orderItem.getDeliveryCharge());
        orderItem.setPlatformCharge(orderItem.getPlatformCharge());
        orderItem.setMenu(existingOrderItem.getMenu());


       // orderItem.setUser((User) getOrderItemById(id));
        //orderItem.getUser().getId();
        //orderItem.setOrder();
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteOrderItem(Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new OrderItemNotFoundException("Order item not found");
        }
        orderItemRepository.deleteById(id);
    }

//    @Override
//    public OrderItem getOrderItemById(Long id) {
//        return orderItemRepository.findById(id)
//                .orElseThrow(() -> new OrderItemNotFoundException("Order item not found"));
//    }

    @Override
    public Map<String, Object> getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException("Order item not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", orderItem.getId());
        response.put("quantity", orderItem.getQuantity());
        response.put("price", orderItem.getPrice());
        response.put("totalPrice", orderItem.getTotalPrice());
        response.put("gst", orderItem.getGst());
        response.put("deliveryCharge", orderItem.getDeliveryCharge());
        response.put("userId", orderItem.getUser().getId());
        response.put("platformCharge", orderItem.getPlatformCharge());
        response.put("grandTotalPrice", orderItem.getGrandTotalPrice());
        response.put("menuId", orderItem.getMenu() != null ? orderItem.getMenu().getMenuId() : null);
        response.put("orderId", orderItem.getOrder() != null ? orderItem.getOrder().getOrderId() : null);
        response.put("menuName", orderItem.getMenu() != null ? orderItem.getMenu().getItemName() : null);

        return response;
    }

    //    @Override
//    public List<OrderItem> getAllOrderItems() {
//        return orderItemRepository.findAll();
//    }
//}
    @Override
    public List<Map<String, Object>> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", orderItem.getId());
            response.put("quantity", orderItem.getQuantity());
            response.put("price", orderItem.getPrice());
            response.put("totalPrice", orderItem.getTotalPrice());
            response.put("gst", orderItem.getGst());
            response.put("deliveryCharge", orderItem.getDeliveryCharge());
            response.put("platformCharge", orderItem.getPlatformCharge());
            response.put("grandTotalPrice", orderItem.getGrandTotalPrice());
            response.put("userId", orderItem.getUser().getId());
            response.put("menuId", orderItem.getMenu() != null ? orderItem.getMenu().getMenuId() : null);
            response.put("orderId", orderItem.getOrder() != null ? orderItem.getOrder().getOrderId() : null);
            response.put("menuName", orderItem.getMenu() != null ? orderItem.getMenu().getItemName() : null);

            responseList.add(response);
        }

        return responseList;
    }
}