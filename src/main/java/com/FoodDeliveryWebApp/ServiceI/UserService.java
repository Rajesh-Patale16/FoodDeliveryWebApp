package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {


    public User saveUser(User user);



    public User FindByUsername(String username);

    public User fetchUserByEmailAndPassword(String email, String password);

}
