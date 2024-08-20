package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.Reply;

public interface ReplyService {

    public Reply getReplyByMessageId(Long messageId) ;

    public Reply createReply(Reply reply);
}
