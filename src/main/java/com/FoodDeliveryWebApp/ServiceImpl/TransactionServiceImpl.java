package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.TransactiobDetails;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.stereotype.Service;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;

@Service
public class TransactionServiceImpl {

    private static final String KEY="rzp_test_rPVpoELQpw9Mm5";
    private static final String KEY_SECRET="pN79jJ1oP4WrQYc5biDOqXbD";
    private static final String CURRENCY="INR";

    public TransactiobDetails createtransaction(Double amount){
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("amount",amount*100);
            jsonObject.put("currency",CURRENCY);
            RazorpayClient razorpayClient= new RazorpayClient(KEY,KEY_SECRET);
            Order order=razorpayClient.orders.create(jsonObject);
            System.out.println(order);
            return prepareTransactiondetails(order);
        }


        catch (Exception e){

            System.out.println(e.getMessage());
        }
        return null;
    }

    private TransactiobDetails prepareTransactiondetails(Order order){
        String orderId=order.get(("id"));
        String currency = order.get("currency");
        Integer amount = order.get("amount");
        TransactiobDetails transactiobDetails= new TransactiobDetails(orderId,currency,amount,KEY);
        return transactiobDetails;

    }





}
