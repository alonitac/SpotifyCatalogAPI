package com.example.catalog.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceSelector {

    @Value("${datasource.type:json}") // Default: JSON
    private String dataSourceType;

    @Bean
    public DataSourceService dataSourceService(JSONDataSourceService jsonService,
                                               SpotifyAPIDataSources spotifyService) {
        if ("json".equalsIgnoreCase(dataSourceType)) {
            return jsonService;
        } else if ("spotify_api".equalsIgnoreCase(dataSourceType)) {
            return  spotifyService;
        } else {
            throw new IllegalArgumentException("Invalid data source type: " + dataSourceType);
        }
    }
}
