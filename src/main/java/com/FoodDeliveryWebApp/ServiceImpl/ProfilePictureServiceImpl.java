package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Entity.ProfilePicture;
import com.FoodDeliveryWebApp.Repository.ProfilePictureRepository;
import com.FoodDeliveryWebApp.ServiceI.ProfilePictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {

    @Autowired
    private ProfilePictureRepository profilePictureRepository;


    @Override
    @Transactional
    public ProfilePicture saveProfilePicture(ProfilePicture profilePicture) {
        try {
            return profilePictureRepository.save(profilePicture);
        } catch (Exception e) {
            // Handle specific exceptions or log the error
            throw new RuntimeException("Failed to save profile picture: " + e.getMessage());
        }
    }

    @Override
    public Optional<ProfilePicture> getProfilePictureById(int id) {
        try {
            return profilePictureRepository.findById(id);
        } catch (Exception e) {
            // Handle specific exceptions or log the error
            throw new RuntimeException("Failed to retrieve profile picture: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteProfilePicture(ProfilePicture existingProfilePicture) {
        try {
            profilePictureRepository.delete(existingProfilePicture);
        } catch (Exception e) {
            // Handle specific exceptions or log the error
            throw new RuntimeException("Failed to delete profile picture: " + e.getMessage());
        }
    }
}
