package com.uber.LocationService.hereMapDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    private String title;
    private String id;
    private String resultType;
    private Address address;
    private Position position;
    private List<Access> access;
    private Double distance;
    private List<Category> categories;
    private List<Reference> references;
    private Highlights highlights;
    private String localityType;
    private MapView mapView;
}
