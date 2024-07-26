package com.FoodDeliveryWebApp.Controller;

import com.FoodDeliveryWebApp.ServiceI.LocationService;
import com.FoodDeliveryWebApp.Entity.Location;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/Locations")
@CrossOrigin("*")
@Slf4j
public class LocationController {

    @Autowired
    LocationService locationService;

    @PostMapping("/Location/saveLocation")
    public ResponseEntity<?> saveLocation(@RequestBody Location location) {
        log.info("Request to save location : {}", location);
        try {
            Location currLocation =  locationService.saveLocations(location);
            return new ResponseEntity<>(currLocation, HttpStatus.CREATED);
        }catch (Exception e) {
            log.error("Error saving location for username '{}'", location.getUsername(), e);
            throw e;
        }
    }

    @PutMapping("/updateLocation/{locationId}")
    public ResponseEntity<Location> updateLocationByLocationId(@PathVariable Long locationId, @RequestBody Location location) {
        log.info("Request to update location with ID {}: {}", locationId, location);
        try {
            Location updatedlocation = locationService.updateLocationById(locationId, location);
            return new ResponseEntity<>(updatedlocation, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Updating location for locationId '{}'", locationId, e);
            throw e;
        }
    }

    @DeleteMapping("/deleteLocation/{locationId}")
    public ResponseEntity<String> deleteByLocationId(@PathVariable Long locationId) {
        log.info("Request to delete location with ID {}", locationId);
        try {
            String message =  locationService.deleteLocationById(locationId);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error deleting location with ID '{}'", locationId, e);
            throw e;
        }
    }

    @GetMapping("/Location/getLocation/{userName}")
    public ResponseEntity<List<Location>> getLocationByUserName(@PathVariable String userName) {
        log.info("Request to get user location details with userName: {}", userName);
        try {
            List<Location> allLocations = locationService.getLocationByUserName(userName);
            return new ResponseEntity<>(allLocations, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving locations for userName '{}'", userName, e);
            throw e;
        }
    }

    @GetMapping("/Location/getAllLocations")
    public ResponseEntity<List<Location>> getAllLocations() {
        log.info("Request to get all location details for all users");
        try {
            List<Location> locations = locationService.getAllLocations();
            return new ResponseEntity<>(locations, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving all locations for all users", e);
            throw e;
        }
    }

}

