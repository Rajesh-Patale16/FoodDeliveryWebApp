package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.ProfilePicture;

import java.util.Optional;

public interface ProfilePictureService {

    ProfilePicture saveProfilePicture(ProfilePicture profilePicture);

    Optional<ProfilePicture> getProfilePictureById(int id);

    void deleteProfilePicture(ProfilePicture existingProfilePicture);
}
