package com.FoodDeliveryWebApp.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user_orders")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference(value = "restaurant_orders")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order")
    @JsonManagedReference(value = "order_orderItem")
    private List<OrderItem> orderItems;

    private LocalDateTime orderDateAndTime;

}