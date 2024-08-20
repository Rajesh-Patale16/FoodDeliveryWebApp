package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.Message;
import com.FoodDeliveryWebApp.Repository.MessageRepository;
import com.FoodDeliveryWebApp.ServiceI.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;



    @Override
    public List<Message> getMessagesByUserId(Long userId) {
        return messageRepository.findByUserId(userId);
    }
    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    @Override
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message getMessageById(Long messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

}
