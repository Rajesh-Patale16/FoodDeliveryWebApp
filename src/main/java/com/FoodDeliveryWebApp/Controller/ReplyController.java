package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.Message;
import com.FoodDeliveryWebApp.Entity.Reply;
import com.FoodDeliveryWebApp.ServiceI.MessageService;
import com.FoodDeliveryWebApp.ServiceI.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/messages/replies/{messageId}")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/")
    public ResponseEntity<Reply> createReply(@PathVariable Long messageId, @RequestBody Reply reply) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return ResponseEntity.notFound().build();
        }
        reply.setMessage(message);
        reply.setReplyTime(LocalDateTime.now());
        Reply newReply = replyService.createReply(reply);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReply);
    }
}

