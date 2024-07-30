package com.FoodDeliveryWebApp.Repository;

import com.FoodDeliveryWebApp.Entity.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Integer> {
}
