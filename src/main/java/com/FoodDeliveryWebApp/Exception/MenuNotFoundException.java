package com.FoodDeliveryWebApp.Exception;

public class MenuNotFoundException extends RuntimeException {

    public MenuNotFoundException(String message) {
        super(message);
    }
}