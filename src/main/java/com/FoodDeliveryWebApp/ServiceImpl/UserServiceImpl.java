package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.Exception.UserNotFoundException;
import com.FoodDeliveryWebApp.Repository.UserRepository;
import com.FoodDeliveryWebApp.ServiceI.RestaurantService;
import com.FoodDeliveryWebApp.ServiceI.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import static com.FoodDeliveryWebApp.CommanUtil.ValidationClass.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    @Override
    public User registerUser(User user) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String username, String password) throws UserNotFoundException {
        return userRepository.findByUsernameAndPassword(username, password).
                orElseThrow(() -> new UserNotFoundException("User " + username + "Not found"));
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User " +userId));
    }

    @Override
    public User updateUser(User user) {

        return userRepository.save(user);
    }

    @Override
    public User UpdateUserDetails(Long userId, User user) {

        logger.info("Updating user by id: {}, data: {}", userId, user);
        try {
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User " + userId));
            if (user.getName() != null) {
                existingUser.setName(user.getName());
            }
            if (user.getUsername() != null){
                existingUser.setUsername(user.getUsername());
            }
            if (user.getEmail() != null){
                existingUser.setEmail(user.getEmail());
            }
            if (user.getGender()!= null){
                existingUser.setGender(user.getGender());
            }
            if (user.getMobileNo()!= null){
                existingUser.setMobileNo(user.getMobileNo());
            }
            if (user.getAddress()!= null){
                existingUser.setAddress(user.getAddress());
            }
            if (user.getPassword()!= null){
                existingUser.setPassword(user.getPassword());
            }
            return userRepository.save(existingUser);
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", userId);
            throw e;
        } catch (Exception e) {
            logger.error("Error updating user with id: {}", userId, e);
            throw new RuntimeException("Failed to update user with id: " + userId, e);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        logger.info("Deleting user by id: {}", userId);
        try {
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User " + userId));
            userRepository.deleteById(userId);
            logger.info("Successfully deleted user with id: {}", userId);
        } catch (Exception e) {
            throw new InvalidParameterException("Error while deleting user, please enter valid userId");
        }
    }


    private void validateUserData(User user) {

        if (user.getUsername() == null || !USERNAME_PATTERN.matcher(user.getUsername()).matches()) {
            throw new IllegalArgumentException("Username is required.");
        }

        if (user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Email is not valid.");
        }

        if (user.getGender() == null || !GENDER_PATTERN.matcher(user.getGender()).matches()) {
            throw new IllegalArgumentException("Gender is required.");
        }

        if (user.getMobileNo() == null || !PHONE_PATTERN.matcher(user.getMobileNo()).matches()) {
            throw new IllegalArgumentException("Mobile number should be 10 digits.");
        }

        if (user.getAddress() == null || !ADDRESS_PATTERN.matcher(user.getAddress().toString()).matches()) {
            throw new IllegalArgumentException("Address is required.");
        }

        if (user.getPassword() == null || !PASSWORD_PATTERN.matcher(user.getPassword()).matches()) {
            throw new IllegalArgumentException("Password is required.");
        }
    }

}
