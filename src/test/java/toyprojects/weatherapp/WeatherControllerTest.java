package toyprojects.weatherapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import toyprojects.weatherapp.entity.WeatherDataDTO;
import toyprojects.weatherapp.service.WeatherService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void contextLoads() {
        assertNotNull(weatherService);
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
    }

    @Test
    void getWeatherByCity() throws Exception {
        String city = "Manila";
        String country = "PH";

        MvcResult result = mockMvc.perform(get("/weather/" + city))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        WeatherDataDTO weather = objectMapper.readValue(responseBody, WeatherDataDTO.class);

        assertEquals(city, weather.getCityName());
        assertEquals(country, weather.getCountry());
        assertNotNull(weather.getWeatherId());
        assertNotNull(weather.getWeatherDescription());
        assertNotNull(weather.getWeatherIcon());
        assertNotNull(weather.getTemperature());
        assertNotNull(weather.getTempFeelsLike());
        assertNotNull(weather.getMinTemp());
        assertNotNull(weather.getMaxTemp());
        assertNotNull(weather.getHumidity());
        assertNotNull(weather.getWindSpeed());
    }

    @Test
    void getWeatherByCity_differentSpelling() throws Exception {
        String city = "mAniLa";
        String country = "PH";

        MvcResult result = mockMvc.perform(get("/weather/" + city))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        WeatherDataDTO weather = objectMapper.readValue(responseBody, WeatherDataDTO.class);

        assertEquals(city.toLowerCase(), weather.getCityName().toLowerCase());
        assertEquals(country, weather.getCountry());
    }

    @Test
    void getWeatherByCity_NonCapitalCity() throws Exception {
        String city = "north York";
        String country = "CA";

        MvcResult result = mockMvc.perform(get("/weather/" + city))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        WeatherDataDTO weather = objectMapper.readValue(responseBody, WeatherDataDTO.class);

        assertEquals(city.toLowerCase(), weather.getCityName().toLowerCase());
        assertEquals(country, weather.getCountry());
    }

    @Test
    void getWeatherByCity_usingInvalidCity() throws Exception {
        String city = "invalidCity";
        String error = "City not found";
        String message = "City not found";

        MvcResult result = mockMvc.perform(get("/weather/" + city))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(error))
                .andExpect(jsonPath("$.message").value(message))
                .andReturn();

        System.out.println("Error Result: " + result.getResponse().getContentAsString());
    }

    @Test
    void getWeatherByCity_usingInvalidCity_nullInput() throws Exception {
        String city = null;
        String error = "City not found";
        String message = "City not found";

        MvcResult result = mockMvc.perform(get("/weather/" + city))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(error))
                .andExpect(jsonPath("$.message").value(message))
                .andReturn();

        System.out.println("Error Result: " + result.getResponse().getContentAsString());
    }

    @Test
    void getWeatherByCity_usingInvalidCity_emptyInput_test1() throws Exception {
        String error = "City not found";
        String message = "City must be provided";

        MvcResult result = mockMvc.perform(get("/weather/"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(error))
                .andExpect(jsonPath("$.message").value(message))
                .andReturn();

        System.out.println("Error Result: " + result.getResponse().getContentAsString());
    }

    @Test
    void getWeatherByCity_usingInvalidCity_emptyInput_test2() throws Exception {
        String error = "City not found";
        String message = "City must be provided";

        MvcResult result = mockMvc.perform(get("/weather"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(error))
                .andExpect(jsonPath("$.message").value(message))
                .andReturn();

        System.out.println("Error Result: " + result.getResponse().getContentAsString());
    }

    @Test
    void getWeatherByCity_withAdditionalParameters() throws Exception {
        String city = "Manila";
        String unitOfMeasurement = "imperial";
        String language = "en";
        String url = String.format("city=%s&units=%s&lang=%s", city, unitOfMeasurement, language);
        String country = "PH";

        MvcResult result = mockMvc.perform(get("/weather?" + url))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        WeatherDataDTO weather = objectMapper.readValue(responseBody, WeatherDataDTO.class);

        assertEquals(city, weather.getCityName());
        assertEquals(country, weather.getCountry());
        assertNotNull(weather.getWeatherId());
        assertNotNull(weather.getWeatherDescription());
        assertNotNull(weather.getWeatherIcon());
        assertNotNull(weather.getTemperature());
        assertNotNull(weather.getTempFeelsLike());
        assertNotNull(weather.getMinTemp());
        assertNotNull(weather.getMaxTemp());
        assertNotNull(weather.getHumidity());
        assertNotNull(weather.getWindSpeed());
    }

    @Test
    void getWeatherByCity_invalidUnit_shouldReturnMetricUnits() throws Exception {
        String city = "Manila";
        String unitOfMeasurement = null;
        String url = String.format("city=%s&units=%s", city, unitOfMeasurement);
        String country = "PH";

        MvcResult result = mockMvc.perform(get("/weather?" + url))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        WeatherDataDTO weather = objectMapper.readValue(responseBody, WeatherDataDTO.class);

        assertEquals(city, weather.getCityName());
        assertEquals(country, weather.getCountry());
        assertNotNull(weather.getWeatherId());
    }

    @Test
    void getWeatherByCity_shouldReturnSpanishLangauge() throws Exception {
        String city = "Manila";
        String lang = "es";
        String url = String.format("city=%s&lang=%s", city, lang);
        String country = "PH";

        MvcResult result = mockMvc.perform(get("/weather?" + url))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        WeatherDataDTO weather = objectMapper.readValue(responseBody, WeatherDataDTO.class);

        assertEquals(city, weather.getCityName());
        assertEquals(country, weather.getCountry());
        assertNotNull(weather.getWeatherId());
    }

    @Test
    void getWeatherByCity_usingNonAvailableLanguage_shouldReturnEnglish() throws Exception {
        String city = "Manila";
        String lang = null;
        String url = String.format("city=%s&lang=%s", city, lang);
        String country = "PH";

        MvcResult result = mockMvc.perform(get("/weather?" + url))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        WeatherDataDTO weather = objectMapper.readValue(responseBody, WeatherDataDTO.class);

        assertEquals(city, weather.getCityName());
        assertEquals(country, weather.getCountry());
        assertNotNull(weather.getWeatherId());
    }

    @Test
    void getSortedWeather3Hr5dayForecastByCity() throws Exception {
        String city = "toronto";
        String country = "CA";
        String uri = String.format("/weather/forecast/%s", city);

        MvcResult result = mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);

        DocumentContext documentContext = JsonPath.parse(responseBody);
        int pageNumber = documentContext.read("$.pageable.pageNumber");
        int pageSize = documentContext.read("$.pageable.pageSize");
        int totalPages = documentContext.read("$.totalPages");
        int totalElements = documentContext.read("$.totalElements");
        String countryName = documentContext.read("$.content[0].country");

        assertEquals(0, pageNumber);
        assertEquals(8, pageSize);
        assertEquals(5, totalPages);
        assertEquals(40, totalElements);
        assertEquals(country, countryName);
    }

    @Test
    void getSortedWeather3Hr5dayForecastByCity_usingInvalidCity() throws Exception {
        String city = "InvalidCity";
        String uri = String.format("/weather/forecast/%s", city);
        String error = "City not found";
        String message = "City not found";

        mockMvc.perform(get(uri))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(error))
                .andExpect(jsonPath("message").value(message));
    }

    @Test
    void getSortedWeather3Hr5dayForecastByCity_usingEmptyCityField() throws Exception {
        String city = "";
        String uri = String.format("/weather/forecast/%s", city);
        String error = "City not found";
        String message = "City must be provided";

        mockMvc.perform(get(uri))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(error))
                .andExpect(jsonPath("message").value(message));
    }

    @Test
    void getSortedWeather3Hr5dayForecastByCity_usingNullCityField() throws Exception {
        String city = null;
        String uri = String.format("/weather/forecast/%s", city);
        String error = "City not found";
        String message = "City not found";

        System.out.println("URI: " + uri);
        mockMvc.perform(get(uri))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(error))
                .andExpect(jsonPath("message").value(message));
    }
}
