package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.Exception.UserNotFoundException;

public interface UserService {

    User registerUser(User user);
    User loginUser(String username, String password) throws UserNotFoundException;

}
