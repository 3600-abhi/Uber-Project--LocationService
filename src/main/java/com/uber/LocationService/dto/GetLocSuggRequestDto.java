package com.uber.LocationService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetLocSuggRequestDto {

    @NotBlank(message = "latitude is required field and cannot be blank")
    private String latitude;

    @NotBlank(message = "longitude is required field and cannot be blank")
    private String longitude;

    @NotBlank(message = "location is required field and cannot be blank")
    private String location;
}
