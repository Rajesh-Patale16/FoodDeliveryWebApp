package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.Review;
import com.FoodDeliveryWebApp.Entity.Menu;
import com.FoodDeliveryWebApp.Entity.Restaurant;
import com.FoodDeliveryWebApp.Exception.ReviewNotFoundException;
import com.FoodDeliveryWebApp.ServiceI.ReviewService;
import com.FoodDeliveryWebApp.Repository.MenuRepository;
import com.FoodDeliveryWebApp.Repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;


    @PostMapping("/Review/menu/save/{menuId}")
    public ResponseEntity<Review> addMenuReview(@PathVariable Long menuId, @RequestBody Review review) {
        logger.info("Received request to save menu review for menu ID: {}", menuId);
        try {
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new IllegalArgumentException("Menu not found with ID: " + menuId));
            review.setMenu(menu);
            return ResponseEntity.ok(reviewService.saveReview(review));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid review: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/Review/restaurant/save/{restaurantId}")
    public ResponseEntity<Review> addRestaurantReview(@PathVariable Long restaurantId, @RequestBody Review review) {
        logger.info("Received request to save restaurant review for restaurant ID: {}", restaurantId);
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + restaurantId));
            review.setRestaurant(restaurant);
            return ResponseEntity.ok(reviewService.saveReview(review));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid review: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/Review/find/{Reviewid}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        logger.info("Received request to get review with id: {}", id);
        try {
            return ResponseEntity.ok(reviewService.getReviewById(id));
        } catch (ReviewNotFoundException e) {
            logger.error("Review not found with id: {}", id);
            return ResponseEntity.status(404).body(null);
        }
    }


    @GetMapping("/Review/findAll")
    public ResponseEntity<List<Review>> getAllReviews() {
        logger.info("Received request to get all reviews");
        return ResponseEntity.ok(reviewService.getAllReviews());
    }


    @GetMapping("/Review/findByRestaurant/{restaurantId}")
    public ResponseEntity<List<Review>> getReviewsByRestaurantId(@PathVariable Long restaurantId) {
        logger.info("Received request to get reviews for restaurant with id: {}", restaurantId);
        try {
            return ResponseEntity.ok(reviewService.getReviewsByRestaurantId(restaurantId));
        } catch (ReviewNotFoundException e) {
            logger.error("No reviews found for restaurant with id: {}", restaurantId);
            return ResponseEntity.status(404).body(null);
        }
    }


    @GetMapping("/Review/findByUser/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
        logger.info("Received request to get reviews for user with id: {}", userId);
        try {
            return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
        } catch (ReviewNotFoundException e) {
            logger.error("No reviews found for user with id: {}", userId);
            return ResponseEntity.status(404).body(null);
        }
    }


    @GetMapping("/Review/findByMenu/{menuId}")
    public ResponseEntity<List<Review>> getReviewsByMenuId(@PathVariable Long menuId) {
        logger.info("Received request to get reviews for menu with id: {}", menuId);
        try {
            return ResponseEntity.ok(reviewService.getReviewsByMenuId(menuId));
        } catch (ReviewNotFoundException e) {
            logger.error("No reviews found for menu with id: {}", menuId);
            return ResponseEntity.status(404).body(null);
        }
    }


    @PutMapping("/Review/update/{Reviewid}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
        logger.info("Received request to update review with id: {}", id);
        try {
            return ResponseEntity.ok(reviewService.updateReview(id, review));
        } catch (ReviewNotFoundException e) {
            logger.error("Review not found with id: {}", id);
            return ResponseEntity.status(404).body(null);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid review: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }


    @DeleteMapping("/Review/delete/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        logger.info("Received request to delete review with id: {}", id);
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok().build();
        } catch (ReviewNotFoundException e) {
            logger.error("Review not found with id: {}", id);
            return ResponseEntity.status(404).build();
        }
    }
}
