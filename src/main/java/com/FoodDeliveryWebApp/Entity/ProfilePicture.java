package com.FoodDeliveryWebApp.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class ProfilePicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonManagedReference(value = "userData_profilePicture")
    private User userData;

    @OneToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "restaurantId")
    @JsonManagedReference(value = "restaurantData_profilePicture")
    private Restaurant restaurantData;

    @OneToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "menuId")
    @JsonManagedReference(value = "menuData_profilePicture")
    private Menu menuData;

    /* Store profile picture as a BLOB using @Lob or @Type */
    @Column(columnDefinition = "LONGBLOB")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Nullable
    private byte[] profilePicture;
}
