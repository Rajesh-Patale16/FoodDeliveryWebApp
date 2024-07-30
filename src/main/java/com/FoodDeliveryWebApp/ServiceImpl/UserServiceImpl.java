package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.Exception.UserNotFoundException;
import com.FoodDeliveryWebApp.Repository.UserRepository;
import com.FoodDeliveryWebApp.ServiceI.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import static com.FoodDeliveryWebApp.CommanUtil.ValidationClass.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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

        if (user.getAddress() == null || !ADDRESS_PATTERN.matcher(user.getAddress()).matches()) {
            throw new IllegalArgumentException("Address is required.");
        }

        if (user.getPassword() == null || !PASSWORD_PATTERN.matcher(user.getPassword()).matches()) {
            throw new IllegalArgumentException("Password is required.");
        }
    }
}
