package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.TransactiobDetails;
import org.springframework.stereotype.Service;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class TransactionServiceImpl {

    private static final String KEY = "rzp_test_rPVpoELQpw9Mm5";
    private static final String KEY_SECRET = "pN79jJ1oP4WrQYc5biDOqXbD";
    private static final String CURRENCY = "INR";
    private static final ZoneId TIME_ZONE = ZoneId.of("Asia/Kolkata"); // Specify your time zone here

    public TransactiobDetails createtransaction(Double amount) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", amount * 100); // Amount in paise
            jsonObject.put("currency", CURRENCY);
            RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);
            Order order = razorpayClient.orders.create(jsonObject);
            System.out.println(order);
            return prepareTransactiondetails(order);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private TransactiobDetails prepareTransactiondetails(Order order) {
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");

        // Initialize createdAt
        Date createdAtDate = null;

        // Fetch the created_at value as a Date object
        Object createdAtObj = order.get("created_at");
        System.out.println("Raw created_at value: " + createdAtObj);

        if (createdAtObj instanceof Date) {
            createdAtDate = (Date) createdAtObj;
        } else {
            System.err.println("Unexpected type for created_at: " + createdAtObj.getClass().getName());
        }

        // Convert Date to LocalDateTime using the specified time zone
        LocalDateTime createdAt = createdAtDate.toInstant()
                .atZone(TIME_ZONE)
                .toLocalDateTime();

        // Format LocalDateTime to a readable format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        String formattedCreatedAt = createdAt.format(formatter);

        // Print formatted date for debugging
        System.out.println("Formatted created_at: " + formattedCreatedAt);

        String status = order.get("status");
        TransactiobDetails transactiobDetails = new TransactiobDetails(orderId, currency, amount, KEY, formattedCreatedAt, status);
        return transactiobDetails;
    }
}
