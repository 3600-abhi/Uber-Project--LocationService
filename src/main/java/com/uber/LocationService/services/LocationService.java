package com.uber.LocationService.services;

import com.uber.LocationService.dto.GetNearByDriversRequestDto;
import com.uber.LocationService.dto.SaveDriverLocationRequestDto;

public interface LocationService {
    Object saveDriverLocation(SaveDriverLocationRequestDto saveDriverLocationRequestDto);

    Object getNearByDrivers(GetNearByDriversRequestDto getNearByDriversRequestDto);
}
