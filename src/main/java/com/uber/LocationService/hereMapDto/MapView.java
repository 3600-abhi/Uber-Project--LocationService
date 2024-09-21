package com.uber.LocationService.hereMapDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapView {
    private String west;
    private String south;
    private String east;
    private String north;
}
