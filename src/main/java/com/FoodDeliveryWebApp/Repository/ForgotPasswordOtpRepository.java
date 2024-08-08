package com.FoodDeliveryWebApp.Repository;

import com.FoodDeliveryWebApp.Entity.ForgotPasswordOtp;
import com.FoodDeliveryWebApp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ForgotPasswordOtpRepository extends JpaRepository<ForgotPasswordOtp, Long> {

    Optional<ForgotPasswordOtp> findByOtp(String otp);

    Optional<ForgotPasswordOtp> findByUser(User user);

}