package com.uber.LocationService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetNearByDriversRequestDto {
    private double latitude;
    private double longitude;
}
