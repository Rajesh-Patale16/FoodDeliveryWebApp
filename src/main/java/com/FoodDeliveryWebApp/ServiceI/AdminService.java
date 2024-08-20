package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.Admin;

public interface AdminService {
    String registerAdmin(Admin admin);
    String loginAdmin(String username, String password);

    void deleteAdmin(Long adminId);
}