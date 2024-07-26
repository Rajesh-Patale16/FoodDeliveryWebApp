package com.FoodDeliveryWebApp.Exception;

public class LocationAlreadyExistsException extends  RuntimeException{
    public LocationAlreadyExistsException(String message) {
        super(message);
    }
}