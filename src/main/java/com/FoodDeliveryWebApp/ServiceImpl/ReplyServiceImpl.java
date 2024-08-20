package com.FoodDeliveryWebApp.ServiceImpl;


import com.FoodDeliveryWebApp.Entity.Reply;
import com.FoodDeliveryWebApp.Repository.ReplyRepository;
import com.FoodDeliveryWebApp.ServiceI.ReplyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyRepository replyRepository;
    @Override
    public Reply getReplyByMessageId(Long messageId) {
        return replyRepository.findById(messageId).orElse(null);
    }
    @Override
    public Reply createReply(Reply reply) {
        return replyRepository.save(reply);
    }

}


