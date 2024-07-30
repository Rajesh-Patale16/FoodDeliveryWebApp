package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.Entity.Menu;
import com.FoodDeliveryWebApp.Entity.ProfilePicture;
import com.FoodDeliveryWebApp.Entity.Restaurant;
import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.Exception.MenuNotFoundException;
import com.FoodDeliveryWebApp.Exception.RestaurantNotFoundException;
import com.FoodDeliveryWebApp.ServiceI.MenuService;
import com.FoodDeliveryWebApp.ServiceI.ProfilePictureService;
import com.FoodDeliveryWebApp.ServiceI.RestaurantService;
import com.FoodDeliveryWebApp.ServiceI.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

@RestController
@RequestMapping("/api/profile_pictures")
public class ProfilePictureController {

    @Autowired
    private ProfilePictureService profilePictureService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    private boolean isValidImages(MultipartFile file) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        return contentType != null && isValidImageType(contentType) && isValidFileExtension(fileName);
    }

    private boolean isValidImageType(String contentType) {
        List<String> validTypes = Arrays.asList("image/png", "image/jpeg");
        return validTypes.contains(contentType);
    }

    private boolean isValidFileExtension(String fileName) {
        if (fileName == null) {
            return false;
        }
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();
        List<String> validExtensions = Arrays.asList("png", "jpg", "jpeg");
        return validExtensions.contains(extension);
    }

    @PostMapping(value = "/uploadUserPicture/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadUserProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                                           @PathVariable("userId") Long userId) {
        try {

            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File cannot be empty");
            }

            if (!isValidImages(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type. Only PNG, JPG, and JPEG are allowed.");
            }

            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ProfilePicture profilePicture = new ProfilePicture();
            profilePicture.setProfilePicture(file.getBytes());
            profilePicture.setUserData(user);

            ProfilePicture savedPicture = profilePictureService.saveProfilePicture(profilePicture);
            user.setProfilePicture(savedPicture);
            userService.updateUser(user);

            return new ResponseEntity<>("Profile picture uploaded successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the file");
        }
    }


    @PostMapping(value = "/uploadRestaurantPicture/{restaurantId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadRestaurantProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                                                         @PathVariable("restaurantId") Long restaurantId) throws RestaurantNotFoundException {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File cannot be empty");
            }

            if (!isValidImages(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type. Only PNG, JPG, and JPEG are allowed.");
            }

            Restaurant restaurant = restaurantService.getRestaurantsById(restaurantId);
            if (restaurant == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ProfilePicture profilePicture = new ProfilePicture();
            profilePicture.setProfilePicture(file.getBytes());
            profilePicture.setRestaurantData(restaurant);

            ProfilePicture savedPicture = profilePictureService.saveProfilePicture(profilePicture);
            restaurant.setProfilePicture(savedPicture);
            restaurantService.updateRestaurant(restaurantId, restaurant);

            return new ResponseEntity<>("Profile picture uploaded successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading");
        }
    }

    @PostMapping(value = "/uploadMenuPicture/{menuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadMenuProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                                                         @PathVariable("menuId") Long menuId) throws MenuNotFoundException {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File cannot be empty");
            }

            if (!isValidImages(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type. Only PNG, JPG, and JPEG are allowed.");
            }

            Menu menu = menuService.getMenuById(menuId);
            if (menu == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ProfilePicture profilePicture = new ProfilePicture();
            profilePicture.setProfilePicture(file.getBytes());
            profilePicture.setMenuData(menu);

            ProfilePicture savedPicture = profilePictureService.saveProfilePicture(profilePicture);
            menu.setProfilePicture(savedPicture);
            menuService.updateMenu(menuId, menu);

            return new ResponseEntity<>("Profile picture uploaded successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading");
        }
    }

    @GetMapping("/getUserPicture/{userId}")
    public ResponseEntity<byte[]> getUserProfilePicture(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            if (user != null) {
                ProfilePicture profilePicture = user.getProfilePicture();
                if (profilePicture != null) {
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(profilePicture.getProfilePicture());
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getRestaurantPicture/{restaurantId}")
    public ResponseEntity<byte[]> getRestaurantProfilePicture(@PathVariable Long restaurantId) throws RestaurantNotFoundException {
        try {
            Restaurant restaurant = restaurantService.getRestaurantsById(restaurantId);
            if (restaurant !=null) {
                ProfilePicture profilePicture = restaurant.getProfilePicture();
                if (profilePicture != null) {
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(profilePicture.getProfilePicture());
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getMenuPicture/{menuId}")
    public ResponseEntity<byte[]> getMenuProfilePicture(@PathVariable Long menuId) throws MenuNotFoundException {
        try {
            Menu menu = menuService.getMenuById(menuId);
            if (menu !=null) {
                ProfilePicture profilePicture = menu.getProfilePicture();
                if (profilePicture != null) {
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(profilePicture.getProfilePicture());
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/updateUserPicture/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateUserProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                                                   @PathVariable("userId") Long userId) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File cannot be empty");
            }

            if (!isValidImages(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type. Only PNG, JPG, and JPEG are allowed.");
            }

            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ProfilePicture existingProfilePicture = user.getProfilePicture();
            if (existingProfilePicture == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            existingProfilePicture.setProfilePicture(file.getBytes());
            ProfilePicture updatedPicture = profilePictureService.saveProfilePicture(existingProfilePicture);
            user.setProfilePicture(updatedPicture);
            userService.updateUser(user);

            return new ResponseEntity<>("Profile picture uploaded successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading");
        }
    }

    @PutMapping(value = "/updateRestaurantPicture/{restaurantId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateRestaurantProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                                                         @PathVariable("restaurantId") Long restaurantId) throws RestaurantNotFoundException {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File cannot be empty");
            }

            if (!isValidImages(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type. Only PNG, JPG, and JPEG are allowed.");
            }

            Restaurant restaurant = restaurantService.getRestaurantsById(restaurantId);
            if (restaurant == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ProfilePicture existingProfilePicture = restaurant.getProfilePicture();
            if (existingProfilePicture == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            existingProfilePicture.setProfilePicture(file.getBytes());
            ProfilePicture updatedPicture = profilePictureService.saveProfilePicture(existingProfilePicture);
            restaurant.setProfilePicture(updatedPicture);
            restaurantService.updateRestaurant(restaurantId,restaurant);

            return new ResponseEntity<>("Profile picture uploaded successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading");
        }
    }

    @PutMapping(value = "/updateMenuPicture/{menuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateMenuProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                                                         @PathVariable("menuId") Long menuId) throws RestaurantNotFoundException {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File cannot be empty");
            }

            if (!isValidImages(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type. Only PNG, JPG, and JPEG are allowed.");
            }

            Menu menu = menuService.getMenuById(menuId);
            if (menu == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ProfilePicture existingProfilePicture = menu.getProfilePicture();
            if (existingProfilePicture == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            existingProfilePicture.setProfilePicture(file.getBytes());
            ProfilePicture updatedPicture = profilePictureService.saveProfilePicture(existingProfilePicture);
            menu.setProfilePicture(updatedPicture);
            menuService.updateMenu(menuId, menu);

            return new ResponseEntity<>("Profile picture uploaded successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading");
        }
    }

    @DeleteMapping("/deleteUserPicture/{userId}")
    public ResponseEntity<String> deleteUserProfilePicture(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ProfilePicture existingProfilePicture = user.getProfilePicture();
            if (existingProfilePicture == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            profilePictureService.deleteProfilePicture(existingProfilePicture);
            user.setProfilePicture(null);
            userService.updateUser(user);

            return ResponseEntity.status(HttpStatus.OK).body("profilePicture deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteRestaurantPicture/{restaurantId}")
    public ResponseEntity<String> deleteRestaurantProfilePicture(@PathVariable Long restaurantId) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantsById(restaurantId);
            if (restaurant == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ProfilePicture existingProfilePicture = restaurant.getProfilePicture();
            if (existingProfilePicture == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            profilePictureService.deleteProfilePicture(existingProfilePicture);
            restaurant.setProfilePicture(null);
            restaurantService.updateRestaurant(restaurantId,restaurant);

            return ResponseEntity.status(HttpStatus.OK).body("profilePicture deleted");
        } catch (Exception | RestaurantNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteMenuPicture/{menuId}")
    public ResponseEntity<String> deleteMenuProfilePicture(@PathVariable Long menuId) {
        try {
            Menu menu = menuService.getMenuById(menuId);
            if (menu == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ProfilePicture existingProfilePicture = menu.getProfilePicture();
            if (existingProfilePicture == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            profilePictureService.deleteProfilePicture(existingProfilePicture);
            menu.setProfilePicture(null);
            menuService.updateMenu(menuId, menu);

            return ResponseEntity.status(HttpStatus.OK).body("profilePicture deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}