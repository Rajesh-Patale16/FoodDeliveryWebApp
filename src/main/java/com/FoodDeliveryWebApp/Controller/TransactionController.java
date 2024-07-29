package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.TransactiobDetails;
import com.FoodDeliveryWebApp.ServiceImpl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class TransactionController {
@Autowired
    TransactionServiceImpl transactionService;

    @GetMapping("/createtransaction/{amount}")
    public TransactiobDetails createtransaction(@PathVariable(name = "amount") Double amount){
        return transactionService.createtransaction(amount);
    }


}
