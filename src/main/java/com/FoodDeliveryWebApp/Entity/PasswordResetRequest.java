package com.FoodDeliveryWebApp.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {

    private String password;
    private String confirmPassword;
}
