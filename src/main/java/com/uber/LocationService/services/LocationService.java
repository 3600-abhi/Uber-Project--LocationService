package com.uber.LocationService.services;

import com.uber.LocationService.dto.GetLocSuggRequestDto;
import com.uber.LocationService.dto.GetLocSuggResponseDto;
import com.uber.LocationService.dto.GetNearByDriversRequestDto;
import com.uber.LocationService.dto.SaveDriverLocationRequestDto;
import com.uber.LocationService.hereMapDto.HereMapLocSuggResponse;

public interface LocationService {
    Object saveDriverLocation(SaveDriverLocationRequestDto saveDriverLocationRequestDto);

    Object getNearByDrivers(GetNearByDriversRequestDto getNearByDriversRequestDto);

    GetLocSuggResponseDto getLocationSuggestion(GetLocSuggRequestDto getLocSuggRequestDto);
}
