package com.example.catalog;

import static org.junit.jupiter.api.Assertions.*;

import com.example.catalog.services.DataSourceSelector;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.services.JSONDataSourceService;
import com.example.catalog.services.SpotifyAPIDataSources;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(MockitoExtension.class)
class DataSourceSelectorTest {


    private JSONDataSourceService jsonService;


    private SpotifyAPIDataSources spotifyService;


    private DataSourceSelector dataSourceSelector;

    @Value("${datasource.type:json}")
    private String dataSourceType;

    @Test
    void testCorrectDataSourceSelected() {
        System.out.println(dataSourceType);
        //DataSourceService selectedService = dataSourceSelector.dataSourceService(jsonService, spotifyService);

      /*  if ("json".equalsIgnoreCase(dataSourceType)) {
            assertEquals(jsonService, selectedService);
        } else if ("spotify_api".equalsIgnoreCase(dataSourceType)) {
            assertEquals(spotifyService, selectedService);
        } else {
            assertThrows(IllegalArgumentException.class,
                    () -> dataSourceSelector.dataSourceService(jsonService, spotifyService));
        }
    }*/
    }
}
