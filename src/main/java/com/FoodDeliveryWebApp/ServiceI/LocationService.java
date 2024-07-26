package com.FoodDeliveryWebApp.ServiceI;

import com.FoodDeliveryWebApp.Entity.Location;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface LocationService {
    Location saveLocations( Location location);
    Location updateLocationById( Long locationId,  Location location);
    String deleteLocationById( Long locationId);
    List<Location> getLocationByUserName( String userName);
    List<Location> getAllLocations();
}
