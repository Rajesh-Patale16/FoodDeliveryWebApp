package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.Exception.UserNotFoundException;

public interface UserService {

   // User registerUser(User user);

    String registerTemporaryUser(User user);

    String verifyOtpToRegister(String email, String otp);

    User loginUser(String username, String password) throws UserNotFoundException;

    User getUserById(Long userId);

    User updateUser(User user);

    User updateUserDetails(Long userId, User user);

    void deleteUser(Long userId);

    void createPasswordResetOtpForUser(User user, String otp);

    String validatePasswordResetOtp(String otp);

    void changeUserPassword(User user, String newPassword);

    User getUserByEmail(String email) throws UserNotFoundException;

    String requestPasswordReset(String email);


}
