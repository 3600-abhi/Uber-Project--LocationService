package com.uber.LocationService.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetLocSuggResponseDto {
    private List<LocationDetail> suggestedLocationList;
}
