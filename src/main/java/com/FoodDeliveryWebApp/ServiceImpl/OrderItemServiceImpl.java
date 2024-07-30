package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.OrderItem;
import com.FoodDeliveryWebApp.Repository.OrderItemRepository;
import com.FoodDeliveryWebApp.ServiceI.OrderItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    private final Logger logger = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        logger.info("Saving order item: {}", orderItem);
        System.out.println(orderItem);
        try {
            validateOrderItem(orderItem);
            // Calculate grand total if it's not already set
            if (orderItem.getGrandTotalPrice() == null) {
                double calculatedGrandTotal = calculateGrandTotal(orderItem);
                orderItem.setGrandTotalPrice(calculatedGrandTotal);
            }
            validateGrandTotalPrice(orderItem.getGrandTotalPrice(),
                    orderItem.getTotalPrice(),
                    orderItem.getGst(),
                    orderItem.getDeliveryCharge(),
                    orderItem.getPlatformCharge());
            return orderItemRepository.save(orderItem);
        } catch (Exception e) {
            logger.error("Error saving order item: {}", orderItem, e);
            throw new RuntimeException("Failed to save order item", e);
        }
    }

    private void validateOrderItem(OrderItem orderItem) {
        if (orderItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (orderItem.getOrder() == null) {
            throw new IllegalArgumentException("Order must be provided");
        }
        if (orderItem.getMenu() == null) {
            throw new IllegalArgumentException("Menu item must be provided");
        }
        if (orderItem.getPrice() == null || orderItem.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (orderItem.getTotalPrice() == null || orderItem.getTotalPrice() <= 0) {
            throw new IllegalArgumentException("Total price must be greater than 0");
        }
        if (orderItem.getGst() == null || orderItem.getGst() < 0) {
            throw new IllegalArgumentException("GST cannot be negative");
        }
        if (orderItem.getDeliveryCharge() == null || orderItem.getDeliveryCharge() < 0) {
            throw new IllegalArgumentException("Delivery charge cannot be negative");
        }
        if (orderItem.getPlatformCharge() == null || orderItem.getPlatformCharge() < 0) {
            throw new IllegalArgumentException("Platform charge cannot be negative");
        }
    }

    private void validateGrandTotalPrice(Double grandTotalPrice,
                                         Double totalPrice,
                                         Double gst,
                                         Double deliveryCharge,
                                         Double platformCharge) {
        if (grandTotalPrice == null) {
            throw new IllegalArgumentException("Grand total price is required");
        }
        if (grandTotalPrice <= 0) {
            throw new IllegalArgumentException("Grand total price must be greater than 0");
        }

        double calculatedGrandTotal = calculateGrandTotal(totalPrice, gst, deliveryCharge, platformCharge);

        if (!grandTotalPrice.equals(calculatedGrandTotal)) {
            throw new IllegalArgumentException("Grand total price must be equal to the sum of total price, GST, delivery charge, and platform charge.");
        }
    }

    private double calculateGrandTotal(OrderItem orderItem) {
        return calculateGrandTotal(orderItem.getTotalPrice(),
                orderItem.getGst(),
                orderItem.getDeliveryCharge(),
                orderItem.getPlatformCharge());
    }

    private double calculateGrandTotal(Double totalPrice, Double gst, Double deliveryCharge, Double platformCharge) {
        return (totalPrice == null ? 0 : totalPrice)
                + (gst == null ? 0 : gst)
                + (deliveryCharge == null ? 0 : deliveryCharge)
                + (platformCharge == null ? 0 : platformCharge);
    }
}
