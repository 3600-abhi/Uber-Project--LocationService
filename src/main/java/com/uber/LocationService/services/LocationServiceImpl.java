package com.uber.LocationService.services;

import com.uber.LocationService.dto.DriverLocationDto;
import com.uber.LocationService.dto.GetNearByDriversRequestDto;
import com.uber.LocationService.dto.SaveDriverLocationRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LocationServiceImpl implements LocationService {

    private static final String DRIVER_GEO_LOCATION_KEY = "DRIVER_LOCATION";

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${custom.search-driver.radius}")
    private double searchNearByDriversInRadius;

    public LocationServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object saveDriverLocation(SaveDriverLocationRequestDto saveDriverLocationRequestDto) {
        try {
            GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
            geoOps.add(DRIVER_GEO_LOCATION_KEY, new RedisGeoCommands.GeoLocation<>(saveDriverLocationRequestDto.getDriverId(), new Point(saveDriverLocationRequestDto.getLongitude(), saveDriverLocationRequestDto.getLatitude())));
            return Map.of("status", "true");
        } catch (Exception e) {
            return Map.of("status", "false");

        }
    }

    @Override
    public Object getNearByDrivers(GetNearByDriversRequestDto getNearByDriversRequestDto) {
        try {
            GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
            Distance radius = new Distance(searchNearByDriversInRadius, Metrics.KILOMETERS);
            Circle circle = new Circle(new Point(getNearByDriversRequestDto.getLongitude(), getNearByDriversRequestDto.getLatitude()), radius);

            GeoResults<RedisGeoCommands.GeoLocation<Object>> fetchedDriversList = geoOps.radius(DRIVER_GEO_LOCATION_KEY, circle);

            List<DriverLocationDto> driverList = new ArrayList<>();

            if (fetchedDriversList != null) {
                for (GeoResult<RedisGeoCommands.GeoLocation<Object>> driver : fetchedDriversList) {
                    Point location = geoOps.position(DRIVER_GEO_LOCATION_KEY, driver.getContent().getName()).get(0);

                    DriverLocationDto driverLocationDto = DriverLocationDto.builder()
                                                                           .driverId(driver.getContent().getName().toString())
                                                                           .longitude(location.getX())
                                                                           .latitude(location.getY())
                                                                           .build();

                    driverList.add(driverLocationDto);
                }
            }

            return driverList;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
