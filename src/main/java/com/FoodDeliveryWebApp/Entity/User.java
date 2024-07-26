package com.FoodDeliveryWebApp.Entity;

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
