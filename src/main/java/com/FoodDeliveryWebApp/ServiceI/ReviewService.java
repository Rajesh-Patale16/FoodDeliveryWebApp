package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.Review;
import com.FoodDeliveryWebApp.Exception.ReviewNotFoundException;

import java.util.List;
import java.util.Map;

public interface ReviewService {
    Review saveReview(Review review);
    Review getReviewById(Long id) throws ReviewNotFoundException;
    List<Map<String, Object>> getAllReviews();
    List<Review> getReviewsByRestaurantId(Long restaurantId) throws ReviewNotFoundException;
    List<Review> getReviewsByUserId(Long userId) throws ReviewNotFoundException;
    List<Review> getReviewsByMenuId(Long menuId) throws ReviewNotFoundException;
    Review updateReview(Long id, Review review) throws ReviewNotFoundException, IllegalArgumentException;
    void deleteReview(Long id) throws ReviewNotFoundException;
}
