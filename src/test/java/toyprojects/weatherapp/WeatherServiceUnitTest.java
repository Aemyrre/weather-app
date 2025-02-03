package toyprojects.weatherapp;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import toyprojects.weatherapp.entity.WeatherData;
import toyprojects.weatherapp.entity.WeatherDataDTO;
import toyprojects.weatherapp.service.WeatherServiceImpl;
import toyprojects.weatherapp.validation.WeatherParameterValidation;

public class WeatherServiceUnitTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    @SuppressWarnings("unused")
    private WeatherParameterValidation validator;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWeatherByCity() throws Exception {
        WeatherData expectedWeatherData = new WeatherData(
                "Manila",
                "PH",
                802,
                "scattered clouds",
                "03d",
                new BigDecimal("26.88"),
                new BigDecimal("28.97"),
                new BigDecimal("26"),
                new BigDecimal("27.81"),
                74,
                new BigDecimal("4.02"),
                1737939764L,
                28800
        );

        String apiResponse = "{\"coord\":{\"lon\":120.9822,\"lat\":14.6042},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"base\":\"stations\",\"main\":{\"temp\":26.88,\"feels_like\":28.97,\"temp_min\":26,\"temp_max\":27.81,\"pressure\":1012,\"humidity\":74,\"sea_level\":1012,\"grnd_level\":1015},\"visibility\":10000,\"wind\":{\"speed\":4.02,\"deg\":12,\"gust\":5.81},\"clouds\":{\"all\":40},\"dt\":1737939764,\"sys\":{\"type\":2,\"id\":2008256,\"country\":\"PH\",\"sunrise\":1737930301,\"sunset\":1737971536},\"timezone\":28800,\"id\":1701668,\"name\":\"Manila\",\"cod\":200}";
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(apiResponse);

        WeatherDataDTO requestCity = weatherService.getWeatherByCity("Manila");

        assertThat(requestCity).usingRecursiveComparison().isEqualTo(expectedWeatherData);
    }

    @Test
    void getWeatherByCity_withAdditionalArguments() throws Exception {
        WeatherData expectedWeatherData = new WeatherData(
                "Manila",
                "PH",
                802,
                "scattered clouds",
                "03d",
                new BigDecimal("26.88"),
                new BigDecimal("28.97"),
                new BigDecimal("26"),
                new BigDecimal("27.81"),
                74,
                new BigDecimal("4.02"),
                1737939764L,
                28800
        );

        String apiResponse = "{\"coord\":{\"lon\":120.9822,\"lat\":14.6042},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"base\":\"stations\",\"main\":{\"temp\":26.88,\"feels_like\":28.97,\"temp_min\":26,\"temp_max\":27.81,\"pressure\":1012,\"humidity\":74,\"sea_level\":1012,\"grnd_level\":1015},\"visibility\":10000,\"wind\":{\"speed\":4.02,\"deg\":12,\"gust\":5.81},\"clouds\":{\"all\":40},\"dt\":1737939764,\"sys\":{\"type\":2,\"id\":2008256,\"country\":\"PH\",\"sunrise\":1737930301,\"sunset\":1737971536},\"timezone\":28800,\"id\":1701668,\"name\":\"Manila\",\"cod\":200}";
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn(apiResponse);

        WeatherDataDTO requestCity = weatherService.getWeatherByCity("Manila", "metric", "en");

        assertThat(requestCity).usingRecursiveComparison().isEqualTo(expectedWeatherData);
    }
}
