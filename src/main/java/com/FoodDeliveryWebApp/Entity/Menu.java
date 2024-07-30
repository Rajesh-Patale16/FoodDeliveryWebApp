package com.FoodDeliveryWebApp.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(name = "item_name")
    private String itemName;
    private String description;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    private Restaurant restaurant;


    public Menu(String itemName, String description, Double price) {
        this.itemName = itemName;
        this.description = description;
        this.price = price;
    }

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "menu_orderItem")
    private List<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToOne(mappedBy = "menuData")
    @JsonBackReference(value = "menuData_profilePicture")
    private ProfilePicture profilePicture;
}
