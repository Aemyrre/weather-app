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
    void getWeatherByCity_() throws Exception {
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
}
