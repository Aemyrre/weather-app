package toyprojects.weatherapp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import toyprojects.weatherapp.entity.WeatherDataDTO;
import toyprojects.weatherapp.exception.CityNotFoundException;
import toyprojects.weatherapp.service.WeatherServiceImpl;

@SpringBootTest
public class WeatherServiceIntegrationTest {

    @Autowired
    private WeatherServiceImpl weatherService;

    @Test
    void contextLoads() {
        assertNotNull(weatherService);
    }

    @Test
    void getWeatherByCity() throws Exception {
        String city = "Manila";
        String country = "PH";

        WeatherDataDTO requestCity = weatherService.getWeatherByCity(city);

        assertEquals(city, requestCity.getCityName());
        assertEquals(country, requestCity.getCountry());
        assertNotNull(requestCity.getHumidity());
    }

    @Test
    void getWeatherByCity_differentSpelling() throws Exception {
        String city = "maNiLa";
        String country = "PH";

        WeatherDataDTO requestCity = weatherService.getWeatherByCity(city);

        assertEquals(city.toLowerCase(), requestCity.getCityName().toLowerCase());
        assertEquals(country, requestCity.getCountry());
        assertNotNull(requestCity.getHumidity());
    }

    @Test
    void getWeatherByCity_NonCapitalCity() throws Exception {
        String city = "Pasay";
        String country = "PH";

        WeatherDataDTO requestCity = weatherService.getWeatherByCity(city);

        assertEquals(city, requestCity.getCityName());
        assertEquals(country, requestCity.getCountry());
        assertNotNull(requestCity.getHumidity());
    }

    @Test
    void getWeatherByCity_usingInvalidCity() throws Exception {
        String city = "";
        String errorMessage = "City not found";

        CityNotFoundException ex = assertThrows(CityNotFoundException.class, () -> weatherService.getWeatherByCity(city));

        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    void getWeatherByCity_usingInvalidCity_nullInput() throws Exception {
        String city = null;
        String errorMessage = "City not found";

        CityNotFoundException ex = assertThrows(CityNotFoundException.class, () -> weatherService.getWeatherByCity(city));

        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    void getWeatherByCity_ImperialUnit() throws Exception {
        String city = "Tokyo";
        String country = "JP";
        String units = "imperial";
        String language = "en";

        WeatherDataDTO requestCity = weatherService.getWeatherByCity(city, units, language);

        assertEquals(city, requestCity.getCityName());
        assertEquals(country, requestCity.getCountry());
        assertNotNull(requestCity.getHumidity());
    }

    @Test
    void getWeatherByCity_invalidUnit_shouldReturnStandardUnits() throws Exception {
        String city = "Tokyo";
        String units = "invalidUnits";
        String language = "en";
        String country = "JP";

        WeatherDataDTO requestCity = weatherService.getWeatherByCity(city, units, language);

        assertEquals(city, requestCity.getCityName());
        assertEquals(country, requestCity.getCountry());
        assertNotNull(requestCity.getHumidity());
    }

    @Test
    void getWeatherByCity_ArabicLanguage() throws Exception {
        String city = "Tokyo";
        String country = "JP";
        String units = "imperial";
        String language = "ar";

        WeatherDataDTO requestCity = weatherService.getWeatherByCity(city, units, language);

        assertEquals(city, requestCity.getCityName());
        assertEquals(country, requestCity.getCountry());
        assertNotNull(requestCity.getHumidity());
    }

    @Test
    void getWeatherByCity_shouldReturnEnglishWhenLanguageIsInvalid() throws Exception {
        String city = "Tokyo";
        String country = "JP";
        String units = "metric";
        String language = "filipino";

        WeatherDataDTO requestCity = weatherService.getWeatherByCity(city, units, language);

        assertEquals(city, requestCity.getCityName());
        assertEquals(country, requestCity.getCountry());
        assertNotNull(requestCity.getHumidity());
    }

    @Test
    void getWeather3HourForecastByCity() throws Exception {
        String city = "Manila";
        String country = "PH";
        int size = 40;

        List<WeatherDataDTO> requestCity = weatherService.getWeather3HourForecastByCity(city);

        assertEquals(city, requestCity.get(0).getCityName());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

}
