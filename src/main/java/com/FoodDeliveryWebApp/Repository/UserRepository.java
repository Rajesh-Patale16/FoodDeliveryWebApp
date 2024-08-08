package com.FoodDeliveryWebApp.Repository;

import com.FoodDeliveryWebApp.Entity.User;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
    Optional<User> findByEmail(String email);

}
