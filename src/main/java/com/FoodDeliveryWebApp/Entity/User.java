package com.FoodDeliveryWebApp.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String gender;

    @Column(unique = true)
    private String mobileNo;

    private String address;

    @Column(unique = true)
    private String username;

    private String password;

    private String confirmPassword;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user_orders")
    private List<Orders> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "user_orderItem")
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "userData")
    @JsonBackReference(value = "userData_profilePicture")
    private ProfilePicture profilePicture;
//    @OneToMany
//    @JoinColumn(
//            name = "Transaction",referencedColumnName = "Id"
//    )
//
//    private TransactiobDetails transactiobDetails;

    //one user have many location
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Location> locations = new ArrayList<>();
}
