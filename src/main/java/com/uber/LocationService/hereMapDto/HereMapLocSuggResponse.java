package com.uber.LocationService.hereMapDto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HereMapLocSuggResponse {
    private List<Item> items;
    private List<Object> queryTerms;
}
