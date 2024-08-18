package com.uber.LocationService.controllers;

import com.uber.LocationService.dto.GetNearByDriversRequestDto;
import com.uber.LocationService.dto.SaveDriverLocationRequestDto;
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
@RequestMapping("/api/v1/location")
public class LocationController {

    private static final String DRIVER_GEO_LOCATION_KEY = "DRIVER_LOCATION";

    private final Environment environment;

    private final RedisTemplate<String, Object> redisTemplate;

    private final double searchNearByDriversInRadius;

    public LocationController(Environment environment, RedisTemplate<String, Object> redisTemplate) {
        this.environment = environment;
        this.redisTemplate = redisTemplate;
        this.searchNearByDriversInRadius = Double.parseDouble(this.environment.getProperty("custom.search-driver.radius"));
    }

    @PostMapping("/driver")
    public ResponseEntity<?> saveDriverLocation(@RequestBody SaveDriverLocationRequestDto saveDriverLocationRequestDto) {
        try {
            GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
            geoOps.add(DRIVER_GEO_LOCATION_KEY, new RedisGeoCommands.GeoLocation<>(saveDriverLocationRequestDto.getDriverId(), new Point(saveDriverLocationRequestDto.getLatitude(), saveDriverLocationRequestDto.getLongitude())));
            return new ResponseEntity<>(Map.of("status", "true"), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Map.of("status", "false"), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/nearby/drivers")
    public ResponseEntity<?> getNearByDrivers(@RequestBody GetNearByDriversRequestDto getNearByDriversRequestDto) {
        try {
            GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
            Distance radius = new Distance(searchNearByDriversInRadius, Metrics.KILOMETERS);
            Circle circle = new Circle(new Point(getNearByDriversRequestDto.getLatitude(), getNearByDriversRequestDto.getLongitude()), radius);

            GeoResults<RedisGeoCommands.GeoLocation<Object>> fetchedDriversList = geoOps.radius(DRIVER_GEO_LOCATION_KEY, circle);

            List<String> driverList = new ArrayList<>();

            if (fetchedDriversList != null) {
                for (GeoResult<RedisGeoCommands.GeoLocation<Object>> driver : fetchedDriversList) {
                    driverList.add(driver.getContent().getName().toString());
                }
            }

            return new ResponseEntity<>(driverList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
