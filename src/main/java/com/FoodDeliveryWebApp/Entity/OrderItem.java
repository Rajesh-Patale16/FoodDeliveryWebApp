package com.FoodDeliveryWebApp.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"menu", "order", "user"})
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    @JsonBackReference(value = "menu_orderItem")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = true)
    @JsonBackReference(value = "order_orderItems")
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

    public Long getUserId(){
        return user !=null?user.getId():null;
    }
    public Long getMenuId(){
        return menu !=null?menu.getMenuId():null;
    }
}
