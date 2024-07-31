package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.Exception.UserNotFoundException;
import com.FoodDeliveryWebApp.ServiceI.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/user/login/{username}/{password}")
    public ResponseEntity<?> loginUser(@PathVariable String username, @PathVariable String password) {
        try {
            User user = userService.loginUser(username, password);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // To update user details
    @PutMapping("/user/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User id cannot be null");
            }
            User updatedUser = userService.UpdateUserDetails(userId, user);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // To delete user details by id
    @DeleteMapping("/user/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            if (userId == null) {
                throw new UserNotFoundException("User id cannot be null");
            }
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body("successfully deleted");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

}