package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.Message;

import java.util.List;

public interface MessageService {
    public List<Message> getMessagesByUserId(Long userId) ;

    public List<Message> getAllMessages() ;

    public Message createMessage(Message message);


    public Message getMessageById(Long messageId) ;

}
