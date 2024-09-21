package com.uber.LocationService.hereMapDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Highlights {
    private List<Title> title;
    private Address__1 address;
}
