package com.FoodDeliveryWebApp.ServiceImpl;

import com.FoodDeliveryWebApp.Exception.LocationAlreadyExistsException;
import com.FoodDeliveryWebApp.Exception.ResourceNotFoundException;
import com.FoodDeliveryWebApp.Repository.LocationRepository;
import com.FoodDeliveryWebApp.Repository.UserRepository;
import com.FoodDeliveryWebApp.Entity.Location;
import com.FoodDeliveryWebApp.Entity.User;
import com.FoodDeliveryWebApp.ServiceI.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationRepository locationRepository;

    @Override
    public Location saveLocations(Location location) {
        User user = userRepository.findByUsername(location.getUsername());
        if (user != null) {
            location.setUser(user);
            Location existingLocation = locationRepository.findByUserAndLatitudeAndLongitude(location.getUser(), location.getLatitude(), location.getLongitude());
            if (existingLocation != null) {
                log.warn("Location already exists for userName '{}', existing location ID: {}", location.getUsername(), existingLocation.getId());
                throw  new LocationAlreadyExistsException( "Location already exists with location ID: " + existingLocation.getId());
            } else {
                Location savedLocation = locationRepository.save(location);
                log.info("Location saved successfully for userName  : {}", location.getUsername());
                return savedLocation;
            }
        } else {
            log.warn("User with username '{}' does not exist", location.getUsername());
            throw new ResourceNotFoundException("User with username '" + location.getUsername() + "' does not exist");
        }
    }

    @Override
    public Location updateLocationById(Long locationId, Location location) {
        Location existingLocation = locationRepository.findById(locationId).orElse(null);
        if (existingLocation != null) {
            existingLocation.setLatitude(location.getLatitude());
            existingLocation.setLongitude(location.getLongitude());
            existingLocation.setUser(location.getUser());
            Location updatedLocation = locationRepository.save(existingLocation);
            log.info("Location with ID {} updated successfully", locationId);
            return updatedLocation;
        } else {
            log.warn("Location with ID '{}' does not exist", locationId);
            throw new ResourceNotFoundException("Location with ID '" + locationId + "' does not exist");
        }
    }

    @Override
    public String deleteLocationById(Long locationId) {

        if (locationRepository.existsById(locationId)) {
            locationRepository.deleteById(locationId);
            log.info("Location with ID {} deleted successfully", locationId);
            return "Location deleted successfully";
        } else {
            log.warn("Location with ID {} not found", locationId);
            throw new ResourceNotFoundException("Location with ID '" + locationId + "' does not exist");

        }
    }

    @Override
    public List<Location> getLocationByUserName(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user != null) {
            List<Location> locations = locationRepository.findByUser(user);
            log.info("Found {} locations for User Name {}", locations.size(), userName);
            return locations;
        } else {
            log.info("User Name {} is not present in database", userName);
            throw new ResourceNotFoundException("User with username '" + userName + "' does not exist");
        }
    }

    @Override
    public List<Location> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        if(!locations.isEmpty()) {
            log.info("Found {} locations", locations.size());
            return locations;
        }else {
            log.info("No locations found in the database");
            return null;
        }
    }
}
