package com.uber.LocationService.services;

import com.uber.LocationService.apis.HereMapLocAutoSugggestApi;
import com.uber.LocationService.constant.AppConstant;
import com.uber.LocationService.dto.*;
import com.uber.LocationService.exception.AppException;
import com.uber.LocationService.hereMapDto.HereMapLocSuggResponse;
import com.uber.LocationService.hereMapDto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LocationServiceImpl implements LocationService {

    private static final String DRIVER_GEO_LOCATION_KEY = "DRIVER_LOCATION";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private HereMapLocAutoSugggestApi hereMapLocAutoSugggestApi;

    @Value("${custom.search-driver.radius}")
    private double searchNearByDriversInRadius;

    @Value("${custom.here-map-api-key}")
    private String HERE_MAP_API_KEY;


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

    @Override
    public GetLocSuggResponseDto getLocationSuggestion(GetLocSuggRequestDto getLocSuggRequestDto) {
        String latLng = getLocSuggRequestDto.getLatitude() + "," + getLocSuggRequestDto.getLongitude();


        Call<HereMapLocSuggResponse> callGetLocationSuggestion = hereMapLocAutoSugggestApi.getLocationSuggestion(
                latLng,
                AppConstant.EN,
                getLocSuggRequestDto.getLocation(),
                HERE_MAP_API_KEY
        );

        try {
            Response<HereMapLocSuggResponse> responseGetLocationSuggestion = callGetLocationSuggestion.execute();

            if (responseGetLocationSuggestion.isSuccessful()) {
                HereMapLocSuggResponse hereMapLocSuggResponse = responseGetLocationSuggestion.body();

                if (hereMapLocSuggResponse != null && hereMapLocSuggResponse.getItems() != null) {
                    List<LocationDetail> locationDetailList = new ArrayList<>();

                    for (Item item : hereMapLocSuggResponse.getItems()) {
                        LocationDetail locationDetail = LocationDetail.builder()
                                                                      .title(item.getTitle())
                                                                      .address(item.getAddress() != null && item.getAddress().getLabel() != null ? item.getAddress().getLabel() : "")
                                                                      .distanceInKm(item.getDistance() != null ? String.valueOf(item.getDistance() / 1000) : "")
                                                                      .latitude(item.getPosition() != null && item.getPosition().getLat() != null ? item.getPosition().getLat() : "")
                                                                      .longitude(item.getPosition() != null && item.getPosition().getLng() != null ? item.getPosition().getLng() : "")
                                                                      .build();

                        locationDetailList.add(locationDetail);
                    }

                    return GetLocSuggResponseDto.builder()
                                                .suggestedLocationList(locationDetailList)
                                                .build();
                }
            }
        } catch (IOException e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return null;
    }
}
