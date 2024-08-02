package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.Review;
import com.FoodDeliveryWebApp.Exception.ReviewNotFoundException;
import com.FoodDeliveryWebApp.Repository.ReviewRepository;
import com.FoodDeliveryWebApp.ServiceI.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review saveReview(Review review) throws IllegalArgumentException {
        validateReview(review);
        if (review.getMenu() != null && review.getRestaurant() == null) {
            review.setReviewType("Menu");
        } else if (review.getRestaurant() != null && review.getMenu() == null) {
            review.setReviewType("Restaurant");
        } else {
            throw new IllegalArgumentException("Review must be associated with either a menu or a restaurant, but not both.");
        }
        logger.info("Saving review: {}", review);
        return reviewRepository.save(review);
    }


    @Override
    public Review getReviewById(Long id) throws ReviewNotFoundException {
        logger.info("Fetching review with id: {}", id);
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id : " + id));
    }

    @Override
    public List<Review> getAllReviews() {
        logger.info("Fetching all reviews");
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> getReviewsByRestaurantId(Long restaurantId) throws ReviewNotFoundException {
        logger.info("Fetching reviews for restaurant with id: {}", restaurantId);
        List<Review> reviews = reviewRepository.findByRestaurantRestaurantId(restaurantId);
        if (reviews.isEmpty()) {
            throw new ReviewNotFoundException("No reviews found for restaurant with id : " + restaurantId);
        }
        return reviews;
    }

    @Override
    public List<Review> getReviewsByUserId(Long userId) throws ReviewNotFoundException {
        logger.info("Fetching reviews for user with id: {}", userId);
        List<Review> reviews = reviewRepository.findByUserId(userId);
        if (reviews.isEmpty()) {
            throw new ReviewNotFoundException("No reviews found for user with id: " + userId);
        }
        return reviews;
    }

    @Override
    public List<Review> getReviewsByMenuId(Long menuId) throws ReviewNotFoundException {
        logger.info("Fetching reviews for menu with id: {}", menuId);
        List<Review> reviews = reviewRepository.findByMenuMenuId(menuId);
        if (reviews.isEmpty()) {
            throw new ReviewNotFoundException("No reviews found for menu with id: " + menuId);
        }
        return reviews;
    }

    @Override
    public Review updateReview(Long id, Review review) throws ReviewNotFoundException, IllegalArgumentException {

        logger.info("Updating review with id: {}", id);
        Review existingReview = getReviewById(id);
        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());
        existingReview.setReviewDate(review.getReviewDate());
        return reviewRepository.save(existingReview);
    }

    @Override
    public void deleteReview(Long id) throws ReviewNotFoundException {
        logger.info("Deleting review with id: {}", id);
        Review existingReview = getReviewById(id);
        reviewRepository.delete(existingReview);
    }

    private void validateReview(Review review) throws IllegalArgumentException {
        if (review.getRating() <= 0 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (review.getComment() == null || review.getComment().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }
        if (review.getUser() == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if ((review.getMenu() != null && review.getRestaurant() != null) || (review.getMenu() == null && review.getRestaurant() == null)) {
            throw new IllegalArgumentException("Review must be associated with either a menu or a restaurant, but not both.");
        }
    }

}
