package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.Message;
import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.ServiceI.MessageService;
import com.FoodDeliveryWebApp.ServiceI.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/users/messages/{userId}")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<Message> createMessage(@PathVariable Long userId, @RequestBody Message message) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        message.setUser(user);
        message.setSentTime(LocalDateTime.now());
        Message newMessage = messageService.createMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMessage);
    }

}
