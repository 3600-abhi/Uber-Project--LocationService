package com.uber.LocationService.config;

import com.uber.LocationService.apis.HereMapLocAutoSugggestApi;
import com.uber.LocationService.constant.AppConstant;
import com.uber.LocationService.utility.Utils;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Autowired
    private Utils utils;

    @Bean
    public HereMapLocAutoSugggestApi locationServiceApi() {
        return new Retrofit.Builder()
                .baseUrl(utils.getConfigValue(AppConstant.HERE_MAP_LOCATION_AUTO_SUGGEST_URL_CONFIG_PATH)) // path only take till port number ie : http://127.0.0.1:8004/ from http://127.0.0.1:8004/api/v1/location/
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(HereMapLocAutoSugggestApi.class);
    }
}
