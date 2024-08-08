package com.FoodDeliveryWebApp.Repository;

import com.FoodDeliveryWebApp.Entity.TemporaryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemporaryUserRepository extends JpaRepository<TemporaryUser, Long> {
    Optional<TemporaryUser> findByEmail(String email);
    Optional<TemporaryUser> findByOtp(String otp);

    Optional<TemporaryUser> findByEmailAndOtp(String email, String otp);
}