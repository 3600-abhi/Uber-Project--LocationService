package com.uber.LocationService.hereMapDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reference {
    private Supplier supplier;
    private String id;
}
