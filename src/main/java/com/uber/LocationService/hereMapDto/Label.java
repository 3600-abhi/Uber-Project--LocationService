package com.uber.LocationService.hereMapDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Label {
    private String start;
    private String end;
}
