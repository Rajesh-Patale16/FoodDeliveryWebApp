package com.FoodDeliveryWebApp.ServiceI;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}