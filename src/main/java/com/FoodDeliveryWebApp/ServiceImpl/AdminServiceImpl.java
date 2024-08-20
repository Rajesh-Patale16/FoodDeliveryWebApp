package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.Admin;
import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.Exception.UserNotFoundException;
import com.FoodDeliveryWebApp.Repository.AdminRepository;
import com.FoodDeliveryWebApp.ServiceI.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminRepository adminRepository;

    @Override
    @Transactional
    public String registerAdmin(Admin admin) {
        logger.info("Registering admin with username: {}", admin.getUsername());

        validateAdminData(admin);

        // Check if the username or email already exists
        if (adminRepository.existsByUsername(admin.getUsername()) || adminRepository.existsByEmail(admin.getEmail()) || adminRepository.existsByMobileNo(admin.getMobileNo())) {
            logger.error("Admin with username {} or email {} or mobile number {} already exists", admin.getUsername(), admin.getEmail(), admin.getMobileNo());
            throw new RuntimeException("Admin with this username or email or mobile number already exists");
        }

        // Handle profile picture
        if (admin.getProfilePicture() != null && admin.getProfilePicture().length > 0) {
            logger.info("Profile picture uploaded for admin with username: {}", admin.getUsername());
            admin.setProfilePicture(admin.getProfilePicture());
        } else {
            logger.info("No profile picture provided for admin with username: {}", admin.getUsername());
            admin.setProfilePicture(null);
        }
        // Save the admin entity
        Admin savedAdmin = adminRepository.save(admin);
        logger.info("Successfully registered admin with ID: {}", savedAdmin.getId());
        // Return a success message
        return "Admin registered successfully.";
    }


    @Override
    public String loginAdmin(String username, String password) {
        logger.info("Admin login attempt for username: {}", username);

        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Admin not found with username: {}", username);
                    return new RuntimeException("Admin not found");
                });

        // Direct password comparison without encoding
        if (password.equals(admin.getPassword())) {
            logger.info("Login successful for username: {}", username);
            return "Login successful!";
        } else {
            logger.error("Invalid credentials for username: {}", username);
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public void deleteAdmin(Long adminId) {
        logger.info("Deleting Admin by id: {}", adminId);
        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("Admin with ID " + adminId + " not found"));

        adminRepository.deleteById(adminId);
        logger.info("Successfully deleted user with id: {}", adminId);
    }

    private void validateAdminData(Admin admin) {
        // Implement any necessary validation logic for admin data here
        // For example, check if all required fields are provided
        if (admin.getUsername() == null || admin.getEmail() == null || admin.getPassword() == null) {
            throw new IllegalArgumentException("Required fields are missing");
        }
    }
}