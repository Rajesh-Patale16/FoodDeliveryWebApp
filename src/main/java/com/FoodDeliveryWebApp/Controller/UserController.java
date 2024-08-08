package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.ForgotPasswordOtp;
import com.FoodDeliveryWebApp.Entity.PasswordResetRequest;
import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.Exception.UserNotFoundException;
import com.FoodDeliveryWebApp.Repository.ForgotPasswordOtpRepository;
import com.FoodDeliveryWebApp.ServiceI.EmailService;
import com.FoodDeliveryWebApp.ServiceI.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ForgotPasswordOtpRepository otpRepository;


//    @PostMapping("/user/register")
//    public ResponseEntity<?> registerUser(@RequestBody User user) {
//        try {
//            User registeredUser = userService.registerUser(user);
//            return ResponseEntity.ok(registeredUser);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
//        }
//    }

    @PostMapping("/user/tempRegister")
    public ResponseEntity<String> tempRegisterUser(@RequestBody User user) {
        try {
            String message = userService.registerTemporaryUser(user);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/user/verifyToRegister")
    public ResponseEntity<String> verifyUser(@RequestParam String email, @RequestParam String otp) {
        try {
            String message = userService.verifyOtpToRegister(email, otp);
            return ResponseEntity.ok(message);
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
            User updatedUser = userService.updateUserDetails(userId, user);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // To get user details by id
    @GetMapping("/user/getUserById/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            if (userId == null) {
                throw new UserNotFoundException("User id cannot be null");
            }
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
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

    @PostMapping("/user/verifyMail/{userEmail}")
    public ResponseEntity<String> forgotPassword(@PathVariable String userEmail) {
        logger.info("Received the request to reset password for email: {}", userEmail);
        try {
            if (userService.getUserByEmail(userEmail) == null) {
                return ResponseEntity.status(404).body("User not found with email: " + userEmail);
            }
            String otp = userService.requestPasswordReset(userEmail);
            emailService.sendEmail(userEmail, "Password Reset OTP","Your OTP for password reset is: " + otp);
            return ResponseEntity.ok("Password reset OTP has been sent to your email.");
        } catch (UserNotFoundException e) {
            logger.error("User not found with this email: {}", userEmail);
            return ResponseEntity.status(404).body("User not found with email: " + userEmail);
        } catch (Exception e) {
            logger.error("An error occurred while processing the password reset request for email: {}", userEmail, e);
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/user/verifyOtp")
    public ResponseEntity<String> validateOtp(@RequestParam String otp, @RequestParam String userEmail) {
        logger.info("Received request to validate OTP: {} for email: {}", otp, userEmail);

        // Step 1: Check if the user exists
        User user;
        try {
            user = userService.getUserByEmail(userEmail);
        } catch (UserNotFoundException e) {
            logger.error("User not found with email: {}", userEmail, e);
            return ResponseEntity.status(404).body("User not found with email: " + userEmail);
        }

        // Step 2: Validate the OTP
        try {
            ForgotPasswordOtp passOtp = otpRepository.findByOtp(otp)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid OTP"));

            // Check if OTP has expired
            LocalDateTime now = LocalDateTime.now();
            if (passOtp.getCreatedAt().plusMinutes(5).isBefore(now)) { // Assuming 5 minutes expiration time
                otpRepository.delete(passOtp); // Optional: Clean up expired OTP
                logger.error("Expired OTP: {} for email: {}", otp, userEmail);
                return ResponseEntity.status(400).body("OTP has expired");
            }

            // OTP is valid
            logger.info("OTP is valid for email: {}", userEmail);
            return ResponseEntity.ok("OTP is valid");

        } catch (IllegalArgumentException e) {
            logger.error("Invalid OTP: {} for email: {}", otp, userEmail, e);
            return ResponseEntity.status(400).body("Invalid or expired OTP");
        } catch (Exception e) {
            logger.error("An unexpected error occurred while validating OTP: {} for email: {}", otp, userEmail, e);
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }



    @PostMapping("/user/resetPassword/{userEmail}")
    public ResponseEntity<String> resetPassword(@PathVariable("userEmail") String userEmail,
                                                @RequestBody PasswordResetRequest request) {
        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();
        String otp = request.getOtp();

        logger.info("Received request to reset password for email: {}", userEmail);

        // Step 1: Check if passwords match
        if (!password.equals(confirmPassword)) {
            logger.error("Passwords do not match for email: {}", userEmail);
            return ResponseEntity.status(400).body("Passwords do not match");
        }

        try {
            // Step 3: Retrieve OTP and User
            ForgotPasswordOtp passOtp = otpRepository.findByOtp(otp)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid OTP"));

            // Verify user email matches
            if (!passOtp.getUser().getEmail().equals(userEmail)) {
                return ResponseEntity.status(400).body("OTP does not match the provided email");
            }

            // Step 4: Change the user's password
            User user = passOtp.getUser();
            userService.changeUserPassword(user, password);

            // Optional: Clean up OTP record
            otpRepository.delete(passOtp);

            return ResponseEntity.ok("Password successfully reset");

        } catch (IllegalArgumentException e) {
            logger.error("Invalid OTP: {} for email: {}", otp, userEmail, e);
            return ResponseEntity.status(400).body("Invalid OTP");
        } catch (Exception e) {
            logger.error("An error occurred while resetting password for email: {}", userEmail, e);
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }

}