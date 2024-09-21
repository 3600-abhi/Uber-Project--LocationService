package com.uber.LocationService.apis;

import com.uber.LocationService.hereMapDto.HereMapLocSuggResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HereMapLocAutoSugggestApi {
    @GET("/v1/autosuggest")
    Call<HereMapLocSuggResponse> getLocationSuggestion(
            @Query(value = "at", encoded = true) String latLng,
            @Query(value = "lang", encoded = true) String language,
            @Query(value = "q", encoded = true) String location,
            @Query(value = "apiKey", encoded = true) String apiKey
    );
}
