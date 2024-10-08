package com.uber.LocationService.controllers;

import com.uber.LocationService.constant.AppConstant;
import com.uber.LocationService.dto.GetLocSuggRequestDto;
import com.uber.LocationService.dto.GetNearByDriversRequestDto;
import com.uber.LocationService.dto.SaveDriverLocationRequestDto;
import com.uber.LocationService.services.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoLocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/location/")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @PostMapping(AppConstant.SAVE_DRIVER_LOCATION)
    public ResponseEntity<?> saveDriverLocation(@RequestBody SaveDriverLocationRequestDto saveDriverLocationRequestDto) {
        return new ResponseEntity<>(locationService.saveDriverLocation(saveDriverLocationRequestDto), HttpStatus.CREATED);
    }

    @PostMapping(AppConstant.GET_NEARBY_DRIVERS)
    public ResponseEntity<?> getNearByDrivers(@RequestBody GetNearByDriversRequestDto getNearByDriversRequestDto) {
        return new ResponseEntity<>(locationService.getNearByDrivers(getNearByDriversRequestDto), HttpStatus.OK);
    }

    @PostMapping(AppConstant.GET_LOCATION_SUGGESTION)
    public ResponseEntity<Object> getLocationSuggestion(@RequestBody @Valid GetLocSuggRequestDto getLocSuggRequestDto) {
        return new ResponseEntity<>(locationService.getLocationSuggestion(getLocSuggRequestDto), HttpStatus.OK);
    }
}
