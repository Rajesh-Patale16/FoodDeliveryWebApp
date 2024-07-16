package com.FoodDeliveryWebApp.Repository;

import com.FoodDeliveryWebApp.Entity.User;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Id> {


}
