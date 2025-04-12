package toyprojects.weatherapp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import toyprojects.weatherapp.entity.WeatherDataDTO;
import toyprojects.weatherapp.exception.CityNotFoundException;
import toyprojects.weatherapp.service.WeatherServiceImpl;

@SpringBootTest
public class WeatherServiceIntegrationTest {

    @Autowired
    private WeatherServiceImpl weatherService;

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceIntegrationTest.class);

    @Test
    void contextLoads() {
        assertNotNull(weatherService);
    }

    @Test
    void getListWeatherForecastByCity() throws Exception {
        String city = "Manila";
        String country = "PH";
        int size = 11;

        logger.info("Logger test: This is a debug message");
        logger.info("Entering getListWeatherForecastByCity with city: {}", city);
        List<WeatherDataDTO> requestCity = weatherService.getListWeatherForecastByCity(city, null, null);

        assertEquals(city, requestCity.get(0).getCityName());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getListWeatherForecastByCity_usingDifferentLetterCases() throws Exception {
        String city = "mAnILa";
        String country = "PH";
        int size = 11;

        logger.info("Logger test: This is a debug message");
        logger.info("Entering getListWeatherForecastByCity with city: {}", city);
        List<WeatherDataDTO> requestCity = weatherService.getListWeatherForecastByCity(city, null, null);

        assertEquals(city.toLowerCase(), requestCity.get(0).getCityName().toLowerCase());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getListWeatherForecastByCity_invalidCity_shouldThrowException() throws Exception {
        String city = "";
        String errorMessage = "City not found";

        logger.info("Logger test: This is a debug message");
        logger.info("Entering getListWeatherForecastByCity with city to retrieve error message: {}", city);
        CityNotFoundException ex = assertThrows(CityNotFoundException.class, () -> weatherService.getListWeatherForecastByCity(city, null, null));

        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    void getListWeatherForecastByCity_usingImperialSystem_AndInJapanese() throws Exception {
        String city = "Tokyo";
        String units = "imperial";
        String lang = "ja";
        int size = 11;

        logger.info("Logger test: This is a debug message");
        logger.info("Entering getListWeatherForecastByCity with city: {}", city);
        List<WeatherDataDTO> requestCity = weatherService.getListWeatherForecastByCity(city, units, lang);

        // Tokyo = 東京都
        assertEquals("東京都", requestCity.get(0).getCityName());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getListWeatherForecastByCity_usingInvalidUnitsAndLang_shouldReturnMetricUnitsAndEnglishLanguage() throws Exception {
        String city = "Tokyo";
        String units = "";
        String lang = null;
        String country = "JP";
        int size = 11;

        logger.info("Logger test: This is a debug message");
        logger.info("Entering getListWeatherForecastByCity with city: {}", city);
        List<WeatherDataDTO> requestCity = weatherService.getListWeatherForecastByCity(city, units, lang);

        assertEquals(city, requestCity.get(0).getCityName());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getListWeatherForecastByCoordinates() throws Exception {
        double lat = 14.537752;
        double lon = 121.001381;
        String country = "PH";
        int size = 11;

        logger.info("Logger test: This is a debug message");
        logger.info("Entering getListWeatherForecastByCoordinates with coordinates: lat={}, lon={}", lat, lon);
        List<WeatherDataDTO> requestCity = weatherService.getListWeatherForecastByCoordinates(lat, lon, null, null);

        assertNotNull(requestCity.get(0).getCityName());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getListWeatherForecastByCoordinates_invalidCity_shouldThrowException() throws Exception {
        double lat = -90.1;
        double lon = 180.1;
        String errorMessage = "City not found";

        logger.info("Logger test: This is a debug message");
        logger.info("Entering getListWeatherForecastByCoordinates with coordinates to retrieve error message: lat={}, lon={}", lat, lon);
        CityNotFoundException ex = assertThrows(CityNotFoundException.class, () -> weatherService.getListWeatherForecastByCoordinates(lat, lon, null, null));

        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    void getListWeatherForecastByCoordinates_usingImperialSystem_AndInJapanese() throws Exception {
        double lat = 35.652832;
        double lon = 139.839478;
        String units = "imperial";
        String lang = "ja";
        int size = 11;

        logger.info("Logger test: This is a debug message");
        logger.info("Entering getListWeatherForecastByCoordinates with coordinates: lat={}, lon={}", lat, lon);
        List<WeatherDataDTO> requestCity = weatherService.getListWeatherForecastByCoordinates(lat, lon, units, lang);

        // Urayasu City = 浦安市
        assertEquals("浦安市", requestCity.get(0).getCityName());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getListWeatherForecastByCoordinates_usingInvalidUnitsAndLang_shouldReturnMetricUnitsAndEnglishLanguage() throws Exception {
        double lat = 35.652832;
        double lon = 139.839478;
        String units = "";
        String lang = null;
        String country = "JP";
        int size = 11;

        logger.info("Logger test: This is a debug message");
        logger.info("Entering getListWeatherForecastByCoordinates with coordinates: lat={}, lon={}", lat, lon);
        List<WeatherDataDTO> requestCity = weatherService.getListWeatherForecastByCoordinates(lat, lon, units, lang);

        assertEquals("Urayasu", requestCity.get(0).getCityName());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

}
