package toyprojects.weatherapp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import toyprojects.weatherapp.controller.WeatherController;
import toyprojects.weatherapp.entity.WeatherDataDTO;
import toyprojects.weatherapp.service.WeatherService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Test
    void contextLoads() {
        assertNotNull(weatherService);
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getWeatherByCity() throws Exception {
        String city = "Manila";
        String country = "PH";
        String url = String.format("/weather/search?city=%s", city);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(view().name("weather"))
                .andExpect(model().attributeExists("currentWeather"))
                .andExpect(model().attributeExists("timeOfDay"))
                .andExpect(model().attributeExists("units"))
                .andExpect(model().attributeExists("weatherForecast"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object currentWeather = modelAndView.getModel().get("currentWeather");
        Object timeOfDay = modelAndView.getModel().get("timeOfDay");
        Object units = modelAndView.getModel().get("units");
        Object weatherForecast = modelAndView.getModel().get("weatherForecast");

        assertNotNull(currentWeather);
        assertNotNull(timeOfDay);
        assertNotNull(units);
        assertNotNull(weatherForecast);

        logger.debug("Current Weather: {}", currentWeather);
        logger.debug("Time of Day: {}", timeOfDay);
        logger.debug("Weather Forecast: {}", weatherForecast);

        assertEquals(city, ((WeatherDataDTO) currentWeather).getCityName());
        assertEquals(country, ((WeatherDataDTO) currentWeather).getCountry());
        assertTrue(units.equals("metric"));
        assertTrue(((List<WeatherDataDTO>) weatherForecast).size() == 10);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getWeatherByCoordinates() throws Exception {
        double lat = 14.537752;
        double lon = 121.001381;
        String country = "PH";
        String url = String.format("/weather?lat=%s&lon=%s", lat, lon);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(view().name("weather"))
                .andExpect(model().attributeExists("currentWeather"))
                .andExpect(model().attributeExists("timeOfDay"))
                .andExpect(model().attributeExists("units"))
                .andExpect(model().attributeExists("weatherForecast"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object currentWeather = modelAndView.getModel().get("currentWeather");
        Object timeOfDay = modelAndView.getModel().get("timeOfDay");
        Object units = modelAndView.getModel().get("units");
        Object weatherForecast = modelAndView.getModel().get("weatherForecast");

        assertNotNull(currentWeather);
        assertNotNull(timeOfDay);
        assertNotNull(units);
        assertNotNull(weatherForecast);

        logger.debug("Current Weather: {}", currentWeather);
        logger.debug("Time of Day: {}", timeOfDay);
        logger.debug("Weather Forecast: {}", weatherForecast);

        logger.debug("Fetch city from coordinates: {}", ((WeatherDataDTO) currentWeather).getCityName());
        assertNotNull(((WeatherDataDTO) currentWeather).getCityName());
        assertEquals(country, ((WeatherDataDTO) currentWeather).getCountry());
        assertTrue(units.equals("metric"));
        assertTrue(((List<WeatherDataDTO>) weatherForecast).size() == 10);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getWeatherByCity_differentSpelling() throws Exception {
        String city = "mAniLa";
        String country = "PH";
        String url = String.format("/weather/search?city=%s", city);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(view().name("weather"))
                .andExpect(model().attributeExists("currentWeather"))
                .andExpect(model().attributeExists("timeOfDay"))
                .andExpect(model().attributeExists("units"))
                .andExpect(model().attributeExists("weatherForecast"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object currentWeather = modelAndView.getModel().get("currentWeather");
        Object timeOfDay = modelAndView.getModel().get("timeOfDay");
        Object units = modelAndView.getModel().get("units");
        Object weatherForecast = modelAndView.getModel().get("weatherForecast");

        assertNotNull(currentWeather);
        assertNotNull(timeOfDay);
        assertNotNull(units);
        assertNotNull(weatherForecast);

        logger.debug("Current Weather: {}", currentWeather);
        logger.debug("Time of Day: {}", timeOfDay);
        logger.debug("Weather Forecast: {}", weatherForecast);

        assertEquals(city.toLowerCase(), ((WeatherDataDTO) currentWeather).getCityName().toLowerCase());
        assertEquals(country, ((WeatherDataDTO) currentWeather).getCountry());
        assertTrue(units.equals("metric"));
        assertTrue(((List<WeatherDataDTO>) weatherForecast).size() == 10);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getWeatherByCity_NonCapitalCity() throws Exception {
        String city = "north York";
        String country = "CA";
        String url = String.format("/weather/search?city=%s", city);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(view().name("weather"))
                .andExpect(model().attributeExists("currentWeather"))
                .andExpect(model().attributeExists("timeOfDay"))
                .andExpect(model().attributeExists("units"))
                .andExpect(model().attributeExists("weatherForecast"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object currentWeather = modelAndView.getModel().get("currentWeather");
        Object timeOfDay = modelAndView.getModel().get("timeOfDay");
        Object units = modelAndView.getModel().get("units");
        Object weatherForecast = modelAndView.getModel().get("weatherForecast");

        assertNotNull(currentWeather);
        assertNotNull(timeOfDay);
        assertNotNull(units);
        assertNotNull(weatherForecast);

        logger.debug("Current Weather: {}", currentWeather);
        logger.debug("Time of Day: {}", timeOfDay);
        logger.debug("Weather Forecast: {}", weatherForecast);

        assertEquals(city.toLowerCase(), ((WeatherDataDTO) currentWeather).getCityName().toLowerCase());
        assertEquals(country, ((WeatherDataDTO) currentWeather).getCountry());
        assertTrue(units.equals("metric"));
        assertTrue(((List<WeatherDataDTO>) weatherForecast).size() == 10);
    }

    @Test
    void getWeatherByCity_usingInvalidCity() throws Exception {
        String city = "invalidCity";
        String error = "Oops! Something went wrong!";
        String message = "City not found";

        String url = String.format("/weather/search?city=%s", city);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorTitle"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object errorObject = modelAndView.getModel().get("errorTitle");
        Object messagObject = modelAndView.getModel().get("errorMessage");

        assertEquals(error, errorObject);
        assertEquals(message, messagObject);
    }

    @Test
    void getWeatherByCoordinates_usingInvalidCoordinates() throws Exception {
        double lat = -91;
        double lon = 121.001381;
        String error = "Oops! Something went wrong!";
        String message = "City not found";

        String url = String.format("/weather?lat=%s&lon=%s", lat, lon);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorTitle"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object errorObject = modelAndView.getModel().get("errorTitle");
        Object messagObject = modelAndView.getModel().get("errorMessage");

        assertEquals(error, errorObject);
        assertEquals(message, messagObject);
    }

    @Test
    void getWeatherByCity_usingInvalidCity_nullInput() throws Exception {
        String city = null;
        String error = "Oops! Something went wrong!";
        String message = "City not found";

        String url = String.format("/weather/search?city=%s", city);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorTitle"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object errorObject = modelAndView.getModel().get("errorTitle");
        Object messagObject = modelAndView.getModel().get("errorMessage");

        assertEquals(error, errorObject);
        assertEquals(message, messagObject);
    }

    @Test
    void getWeatherByCity_usingInvalidCity_emptyInput_test1() throws Exception {
        String error = "Oops! Something went wrong!";
        String message = "City not found";

        String url = String.format("/weather/search?city=");

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorTitle"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object errorObject = modelAndView.getModel().get("errorTitle");
        Object messagObject = modelAndView.getModel().get("errorMessage");

        assertEquals(error, errorObject);
        assertEquals(message, messagObject);
    }

    @Test
    void getWeatherByCity_usingInvalidCity_emptyInput_test2() throws Exception {
        String error = "Invalid Input";
        String message = "Required request parameter 'city' for method parameter type String is not present";

        String url = String.format("/weather/search");

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorTitle"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object errorObject = modelAndView.getModel().get("errorTitle");
        Object messagObject = modelAndView.getModel().get("errorMessage");

        assertEquals(error, errorObject);
        assertEquals(message, messagObject);
    }

    @Test
    void getWeatherByCoordinates_usingInvalidCoordinates_emptyInput() throws Exception {
        String error = "Unexpected Error";
        String url = String.format("/weather?lat=&lon=");

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorTitle"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object errorObject = modelAndView.getModel().get("errorTitle");
        Object messagObject = modelAndView.getModel().get("errorMessage");

        assertEquals(error, errorObject);
        assertNotNull(messagObject);
    }

    @Test
    void getWeatherByCoordinates_usingInvalidCoordinates_emptyInpu_part2() throws Exception {
        String error = "Invalid Input";
        String message = "Required request parameter 'lat' for method parameter type double is not present";
        String url = String.format("/weather");

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorTitle"))
                .andExpect(model().attributeExists("errorMessage"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object errorObject = modelAndView.getModel().get("errorTitle");
        Object messagObject = modelAndView.getModel().get("errorMessage");

        assertEquals(error, errorObject);
        assertEquals(message, messagObject);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getWeatherByCity_withAdditionalParameters() throws Exception {
        String city = "Manila";
        String country = "PH";
        String unitOfMeasurement = "imperial";
        String url = String.format("/weather/search?city=%s&units=%s", city, unitOfMeasurement);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(view().name("weather"))
                .andExpect(model().attributeExists("currentWeather"))
                .andExpect(model().attributeExists("timeOfDay"))
                .andExpect(model().attributeExists("units"))
                .andExpect(model().attributeExists("weatherForecast"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object currentWeather = modelAndView.getModel().get("currentWeather");
        Object timeOfDay = modelAndView.getModel().get("timeOfDay");
        Object units = modelAndView.getModel().get("units");
        Object weatherForecast = modelAndView.getModel().get("weatherForecast");

        assertNotNull(currentWeather);
        assertNotNull(timeOfDay);
        assertNotNull(units);
        assertNotNull(weatherForecast);

        logger.debug("Current Weather: {}", currentWeather);
        logger.debug("Time of Day: {}", timeOfDay);
        logger.debug("Weather Forecast: {}", weatherForecast);

        assertEquals(city, ((WeatherDataDTO) currentWeather).getCityName());
        assertEquals(country, ((WeatherDataDTO) currentWeather).getCountry());
        assertEquals(unitOfMeasurement, units);
        assertTrue(((List<WeatherDataDTO>) weatherForecast).size() == 10);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getWeatherByCoordinates_withAdditionalParameters() throws Exception {
        double lat = 14.537752;
        double lon = 121.001381;
        String country = "PH";
        String unitOfMeasurement = "imperial";
        String url = String.format("/weather?lat=%s&lon=%s&units=%s", lat, lon, unitOfMeasurement);

        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(view().name("weather"))
                .andExpect(model().attributeExists("currentWeather"))
                .andExpect(model().attributeExists("timeOfDay"))
                .andExpect(model().attributeExists("units"))
                .andExpect(model().attributeExists("weatherForecast"))
                .andReturn();

        ModelAndView modelAndView = result.getModelAndView();
        assertNotNull(modelAndView);

        Object currentWeather = modelAndView.getModel().get("currentWeather");
        Object timeOfDay = modelAndView.getModel().get("timeOfDay");
        Object units = modelAndView.getModel().get("units");
        Object weatherForecast = modelAndView.getModel().get("weatherForecast");

        assertNotNull(currentWeather);
        assertNotNull(timeOfDay);
        assertNotNull(units);
        assertNotNull(weatherForecast);

        logger.debug("Current Weather: {}", currentWeather);
        logger.debug("Time of Day: {}", timeOfDay);
        logger.debug("Weather Forecast: {}", weatherForecast);

        logger.debug("Fetched city: {}", ((WeatherDataDTO) currentWeather).getCityName());
        assertNotNull(((WeatherDataDTO) currentWeather).getCityName());
        assertEquals(country, ((WeatherDataDTO) currentWeather).getCountry());
        assertEquals(unitOfMeasurement, units);
        assertTrue(((List<WeatherDataDTO>) weatherForecast).size() == 10);
    }
}
