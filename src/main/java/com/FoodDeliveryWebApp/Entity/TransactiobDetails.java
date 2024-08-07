package com.FoodDeliveryWebApp.Entity;


import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactiobDetails {


    private String orderId;
    private String currency;
    private Integer amount;
    private String KEY;
    private String createdAt; // New field for date and time
    private String status;


}