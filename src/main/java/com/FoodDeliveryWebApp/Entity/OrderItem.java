package com.FoodDeliveryWebApp.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference(value = "menu_orderItem")
    private Menu menu;

    @ManyToOne
    @JsonBackReference(value = "order_orderItem")
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user_orderItem")
    private User user;

    private int quantity;
    private Double price;
    private Double totalPrice;
    private Double gst;
    private Double deliveryCharge;
    private Double platformCharge;
    private Double grandTotalPrice;

}