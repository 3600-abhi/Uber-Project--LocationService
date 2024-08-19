package com.uber.LocationService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverLocationDto {
    private String driverId;
    private double latitude;
    private double longitude;
}
