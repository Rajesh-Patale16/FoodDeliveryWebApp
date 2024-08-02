package com.FoodDeliveryWebApp.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReviewNotFoundException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(ReviewNotFoundException.class);

    public ReviewNotFoundException(String message) {
        super(message);
        logger.error(message);
    }
}
