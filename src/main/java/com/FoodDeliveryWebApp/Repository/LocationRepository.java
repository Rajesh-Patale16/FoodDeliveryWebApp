package com.FoodDeliveryWebApp.Repository;

import com.FoodDeliveryWebApp.Entity.Location;
import com.FoodDeliveryWebApp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByUser(User user);
    Location findByUserAndLatitudeAndLongitude(User user, Double latitude,Double longitude);
}
