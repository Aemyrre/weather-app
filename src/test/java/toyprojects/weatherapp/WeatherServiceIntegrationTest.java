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
import org.springframework.test.annotation.DirtiesContext;

import toyprojects.weatherapp.entity.WeatherDataDTO;
import toyprojects.weatherapp.exception.CityNotFoundException;
import toyprojects.weatherapp.service.WeatherServiceImpl;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WeatherServiceIntegrationTest {

    @Autowired
    private WeatherServiceImpl weatherService;

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceIntegrationTest.class);

    @Test
    void contextLoads() {
        assertNotNull(weatherService);
    }

    @Test
    void getCurrentWeatherDataByCity() {
        String city = "Manila";
        String country = "PH";

        logger.info("Entering getCurrentWeatherDataByCity with city: {}", city);
        WeatherDataDTO requestCity = weatherService.getCurrentWeatherDataByCity(city, null, null);

        assertEquals(city, requestCity.getCityName());
        assertEquals(country, requestCity.getCountry());
    }

    @Test
    void getCurrentWeatherDataByCity_withCountryCode() {
        String city = "Bangkok, TH";
        String country = "TH";

        logger.info("Entering getCurrentWeatherDataByCity with city: {}", city);
        WeatherDataDTO requestCity = weatherService.getCurrentWeatherDataByCity(city, null, null);

        assertEquals("Bangkok", requestCity.getCityName());
        assertEquals(country, requestCity.getCountry());
    }

    @Test
    void getCurrentWeatherDataByCity_usingDifferentLetterCase() {
        String city = "mAnILa";
        String country = "PH";

        logger.info("Entering getCurrentWeatherDataByCity with city: {}", city);
        WeatherDataDTO requestCity = weatherService.getCurrentWeatherDataByCity(city, null, null);

        assertEquals(city.toLowerCase(), requestCity.getCityName().toLowerCase());
        assertEquals(country, requestCity.getCountry());
    }

    @Test
    void getCurrentWeatherDataByCity_invalidCity_shouldThrowException() {
        String city = "";
        String errorMessage = "City not found";

        logger.info("Entering getCurrentWeatherDataByCity with city to retrieve error message: {}", errorMessage);
        CityNotFoundException ex = assertThrows(CityNotFoundException.class, () -> weatherService.getCurrentWeatherDataByCity(city, null, null));

        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    void getCurrentWeatherDataByCity_nullCity_shouldThrowException() {

        String errorMessage = "City not found";

        logger.info("Entering getCurrentWeatherDataByCity with city to retrieve error message: {}", errorMessage);
        CityNotFoundException ex = assertThrows(CityNotFoundException.class, () -> weatherService.getCurrentWeatherDataByCity(null, null, null));

        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    void getCurrentWeatherDataByCity_usingImperialSystem_AndInJapanes() {
        String city = "Tokyo";
        String units = "imperial";
        String lang = "ja";

        logger.info("Entering getCurrentWeatherDataByCity with city: {}", city);
        WeatherDataDTO requestCity = weatherService.getCurrentWeatherDataByCity(city, units, lang);

        // Tokyo = 東京都
        assertEquals("東京都", requestCity.getCityName());
        assertEquals("JP", requestCity.getCountry());
    }

    @Test
    void getCurrentWeatherDataByCity_usingInvalidUnitsAndLang_shouldReturnMetricUnitsAndEnglishLanguage() {
        String city = "Tokyo";
        String units = "";
        String lang = null;
        String country = "JP";

        logger.info("Entering getCurrentWeatherDataByCity with city: {}", city);
        WeatherDataDTO requestCity = weatherService.getCurrentWeatherDataByCity(city, units, lang);

        assertEquals(city, requestCity.getCityName());
        assertEquals(country, requestCity.getCountry());
    }

    @Test
    void getCurrentWeatherDataByCoordinates() {
        double lat = 14.537752;
        double lon = 121.001381;
        String country = "PH";

        logger.info("Entering getCurrentWeatherDataByCoordinates with coordinates: lat={}, lon={}", lat, lon);
        WeatherDataDTO requestCity = weatherService.getCurrentWeatherDataByCoordinates(lat, lon, null, null);

        assertNotNull(requestCity.getCityName());
        assertEquals(country, requestCity.getCountry());
    }

    @Test
    void getCurrentWeatherDataByCoordinates_invalidCity_shouldThrowException() {
        double lat = -90.1;
        double lon = 180.1;
        String errorMessage = "City not found";

        logger.info("Entering getCurrentWeatherDataByCoordinates with coordinates to retrieve error message: lat={}, lon={} message={}", lat, lon, errorMessage);
        CityNotFoundException ex = assertThrows(CityNotFoundException.class, () -> weatherService.getCurrentWeatherDataByCoordinates(lat, lon, null, null));

        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    void getCurrentWeatherDataByCoordinates_usingImperialSystem_AndInJapanese() {
        double lat = 35.652832;
        double lon = 139.839478;
        String units = "imperial";
        String lang = "ja";

        logger.info("Entering getCurrentWeatherDataByCoordinates with coordinates: lat={}, lon={}", lat, lon);
        WeatherDataDTO requestCity = weatherService.getCurrentWeatherDataByCoordinates(lat, lon, units, lang);

        assertNotNull(requestCity.getCityName());
        assertEquals("JP", requestCity.getCountry());
    }

    @Test
    void getCurrentWeatherDataByCoordinates_usingInvalidUnitsAndLang_shouldReturnMetricUnitsAndEnglishLanguage() {
        double lat = 35.652832;
        double lon = 139.839478;
        String units = "";
        String lang = null;
        String country = "JP";

        logger.info("Entering getCurrentWeatherDataByCoordinates with coordinates: lat={}, lon={}", lat, lon);
        WeatherDataDTO requestCity = weatherService.getCurrentWeatherDataByCoordinates(lat, lon, units, lang);

        assertEquals("Urayasu", requestCity.getCityName());
        assertEquals(country, requestCity.getCountry());
    }

    @Test
    void getListWeatherForecastByCity() {
        String city = "Manila";
        String country = "PH";
        int size = 10;

        logger.info("Entering getListWeatherForecastByCity with city: {}", city);
        List<WeatherDataDTO> requestCity = weatherService.getListWeatherForecastByCity(city, null, null);

        assertEquals(city, requestCity.get(0).getCityName());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getListWeatherForecastByCity_usingDifferentLetterCases() {
        String city = "mAnILa";
        String country = "PH";
        int size = 10;

        logger.info("Entering getListWeatherForecastByCity with city: {}", city);
        List<WeatherDataDTO> requestCity = weatherService.getListWeatherForecastByCity(city, null, null);

        assertEquals(city.toLowerCase(), requestCity.get(0).getCityName().toLowerCase());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getListWeatherForecastByCity_invalidCity_shouldThrowException() {
        String city = "";
        String errorMessage = "City not found";

        logger.info("Entering getListWeatherForecastByCity with city to retrieve error message: {}", city);
        CityNotFoundException ex = assertThrows(CityNotFoundException.class, () -> weatherService.getListWeatherForecastByCity(city, null, null));

        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    void getListWeatherForecastByCity_usingImperialSystem_AndInJapanese() {
        String city = "Tokyo";
        String units = "imperial";
        String lang = "ja";
        int size = 10;

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
        int size = 10;

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
        int size = 10;

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
        int size = 10;

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
        int size = 10;

        logger.info("Entering getListWeatherForecastByCoordinates with coordinates: lat={}, lon={}", lat, lon);
        List<WeatherDataDTO> requestCity = weatherService.getListWeatherForecastByCoordinates(lat, lon, units, lang);

        assertNotNull(requestCity.get(0).getCityName());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

}
