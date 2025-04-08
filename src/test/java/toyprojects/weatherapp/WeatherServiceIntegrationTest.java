package toyprojects.weatherapp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

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
    void getListWeather3Hr5dayForecastByCity() throws Exception {
        String city = "Manila";
        String country = "PH";
        int size = 40;

        @SuppressWarnings("deprecation")
        List<WeatherDataDTO> requestCity = weatherService.getListWeather3Hr5dayForecastByCity(city);

        assertEquals(city, requestCity.get(0).getCityName());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getListWeather3Hr5dayForecastByCity_usingDifferentLetterCases() throws Exception {
        String city = "mAnILa";
        String country = "PH";
        int size = 40;

        @SuppressWarnings("deprecation")
        List<WeatherDataDTO> requestCity = weatherService.getListWeather3Hr5dayForecastByCity(city);

        assertEquals(city.toLowerCase(), requestCity.get(0).getCityName().toLowerCase());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getListWeather3Hr5dayForecastByCity_invalidCity_shouldThrowException() throws Exception {
        String city = "";
        String errorMessage = "City not found";

        @SuppressWarnings("deprecation")
        CityNotFoundException ex = assertThrows(CityNotFoundException.class, () -> weatherService.getListWeather3Hr5dayForecastByCity(city));

        assertEquals(errorMessage, ex.getMessage());
    }

    @Test
    void getListWeather3Hr5dayForecastByCity_usingImperialSystem_AndInJapanese() throws Exception {
        String city = "Tokyo";
        String units = "imperial";
        String lang = "ja";
        int size = 40;

        @SuppressWarnings("deprecation")
        List<WeatherDataDTO> requestCity = weatherService.getListWeather3Hr5dayForecastByCity(city, units, lang);

        assertEquals(city, requestCity.get(0).getCityName());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getListWeather3Hr5dayForecastByCity_usingInvalidUnitsAndLang_shouldReturnMetricUnitsAndEnglishLanguage() throws Exception {
        String city = "Tokyo";
        String units = "";
        String lang = null;
        String country = "JP";
        int size = 40;

        @SuppressWarnings("deprecation")
        List<WeatherDataDTO> requestCity = weatherService.getListWeather3Hr5dayForecastByCity(city, units, lang);

        assertEquals(city, requestCity.get(0).getCityName());
        assertEquals(country, requestCity.get(0).getCountry());
        assertEquals(size, requestCity.size());
    }

    @Test
    void getSortedWeather3Hr5dayForecastByCity() throws Exception {
        String city = "Mandaluyong City";
        String country = "PH";
        int page = 0;
        int size = 10;

        Page<WeatherDataDTO> weatherDataDTOs = weatherService.getSortedWeather3Hr5dayForecastByCity(city, page, size);

        System.out.println(weatherDataDTOs.toString());
        System.out.println("Total No. of Elements in a Page: " + weatherDataDTOs.getNumberOfElements());
        System.out.println("Total No. of Elements: " + weatherDataDTOs.getTotalElements());
        System.out.println("Total No. of Pages: " + weatherDataDTOs.getTotalPages());

        assertEquals(10, weatherDataDTOs.getNumberOfElements());
        assertEquals(40, weatherDataDTOs.getTotalElements());
        assertEquals(4, weatherDataDTOs.getTotalPages());
        assertEquals(city, weatherDataDTOs.getContent().get(0).getCityName());
        assertEquals(country, weatherDataDTOs.getContent().get(0).getCountry());
    }

    @Test
    void getSortedWeather3Hr5dayForecastByCity_usingInvalidPageAndSize_shouldreturnPageZeroAndSizeEight() throws Exception {
        String city = "Mandaluyong City";
        String country = "PH";
        int page = -1;
        int size = -1;

        Page<WeatherDataDTO> weatherDataDTOs = weatherService.getSortedWeather3Hr5dayForecastByCity(city, page, size);

        System.out.println("Total No. of Elements in a Page: " + weatherDataDTOs.getNumberOfElements());
        System.out.println("Total No. of Elements: " + weatherDataDTOs.getTotalElements());
        System.out.println("Total No. of Pages: " + weatherDataDTOs.getTotalPages());

        assertEquals(8, weatherDataDTOs.getNumberOfElements());
        assertEquals(40, weatherDataDTOs.getTotalElements());
        assertEquals(5, weatherDataDTOs.getTotalPages());
        assertEquals(city, weatherDataDTOs.getContent().get(0).getCityName());
        assertEquals(country, weatherDataDTOs.getContent().get(0).getCountry());
    }

    @Test
    void getSortedWeather3Hr5dayForecastByCity_usingOutOfBoundPage() throws Exception {
        String city = "Mandaluyong City";
        int page = 10;
        int size = 8;

        Page<WeatherDataDTO> weatherDataDTOs = weatherService.getSortedWeather3Hr5dayForecastByCity(city, page, size);

        System.out.println("Total No. of Elements in a Page: " + weatherDataDTOs.getNumberOfElements());
        System.out.println("Total No. of Elements: " + weatherDataDTOs.getTotalElements());
        System.out.println("Total No. of Pages: " + weatherDataDTOs.getTotalPages());

        assertEquals(0, weatherDataDTOs.getNumberOfElements());
        assertEquals(40, weatherDataDTOs.getTotalElements());
        assertEquals(5, weatherDataDTOs.getTotalPages());
    }

    @Test
    void getWeatherByCoordinates() throws Exception {
        double lat = 14.5377;
        double lon = 121.0008;

        String expectedCity = "Makati City";
        String expectedCountry = "PH";

        WeatherDataDTO weatherDataDTO = weatherService.getWeatherByCoordinates(lat, lon);

        assertEquals(expectedCity.toLowerCase(), weatherDataDTO.getCityName().toLowerCase());
        assertEquals(expectedCountry.toLowerCase(), weatherDataDTO.getCountry().toLowerCase());
        assertNotNull(weatherDataDTO.getWeatherMainDescription());
        assertNotNull(weatherDataDTO.getWeatherDescription());
    }

    @Test
    void getWeatherByCoordinates_usingImperialSystem_andSpanishLanguage() throws Exception {
        double lat = 14.5377;
        double lon = 121.0008;
        String lang = "es";
        String uom = "imperial";

        String expectedCity = "Makati City";
        String expectedCountry = "PH";

        WeatherDataDTO weatherDataDTO = weatherService.getWeatherByCoordinates(lat, lon, uom, lang);

        assertEquals(expectedCity.toLowerCase(), weatherDataDTO.getCityName().toLowerCase());
        assertEquals(expectedCountry.toLowerCase(), weatherDataDTO.getCountry().toLowerCase());
        assertNotNull(weatherDataDTO.getWeatherMainDescription());
        assertNotNull(weatherDataDTO.getWeatherDescription());
    }

    @Test
    void getWeatherByCoordinates_invalidLatAndLon_throwException() {
        double lat = -90.99;
        double lon = 180.11;

        String expectedErrorResponse = "City not found";

        CityNotFoundException ex = assertThrows(CityNotFoundException.class, () -> weatherService.getWeatherByCoordinates(lat, lon));

        assertEquals(expectedErrorResponse, ex.getMessage());
    }
}
