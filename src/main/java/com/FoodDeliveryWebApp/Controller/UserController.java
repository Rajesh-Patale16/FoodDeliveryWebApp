package com.FoodDeliveryWebApp.Controller;


import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.ServiceI.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

}
