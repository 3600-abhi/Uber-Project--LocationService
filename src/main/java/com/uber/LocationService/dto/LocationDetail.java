package com.uber.LocationService.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDetail {
    private String title;
    private String address;
    private String distanceInKm;
    private String latitude;
    private String longitude;
}
