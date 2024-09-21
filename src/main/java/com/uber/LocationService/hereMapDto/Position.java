package com.uber.LocationService.hereMapDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Position {
    private String lat;
    private String lng;
}
