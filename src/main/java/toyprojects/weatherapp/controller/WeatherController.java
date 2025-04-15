package toyprojects.weatherapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import toyprojects.weatherapp.entity.WeatherDataDTO;
import toyprojects.weatherapp.exception.CityNotFoundException;
import toyprojects.weatherapp.service.WeatherService;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Controller method for fetching weather data using a city as parameter.
     * units and language are optional. defaults: units: metric lang: en
     *
     * @param city
     * @param units
     * @param lang
     * @return weather data, time of day and forecasts data to "index.html"
     */
    @GetMapping("/search")
    public ModelAndView getListWeatherForecastByCity(@RequestParam String city,
            @RequestParam(required = false, defaultValue = "metric") String units,
            @RequestParam(required = false, defaultValue = "en") String lang) {

        if (city == null || city.isEmpty()) {
            throw new CityNotFoundException();
        }

        logger.info("Fetching weather data and preparing view model for city: {}", city);
        List<WeatherDataDTO> weatherDataDTOs = weatherService.getListWeatherForecastByCity(city, units, lang);

        return generateModelAndView(weatherDataDTOs, units, null, null);
    }

    /**
     * Controller method for fetching weather data using latitude and longitude
     * as parameters. units and language are optional. defaults: units: metric
     * lang: en
     *
     * @param lat
     * @param lon
     * @param units
     * @param lang
     * @return weather data, time of day and forecasts data to "index.html"
     */
    @GetMapping
    public ModelAndView getListWeatherForecastByCoordinates(@RequestParam double lat, @RequestParam double lon,
            @RequestParam(required = false, defaultValue = "metric") String units,
            @RequestParam(required = false, defaultValue = "en") String lang) {

        logger.info("Fetching weather data and preparing view model for coordinates: lat={}, lon={}", lat, lon);
        List<WeatherDataDTO> weatherDataDTOs = weatherService.getListWeatherForecastByCoordinates(lat, lon, units, lang);

        return generateModelAndView(weatherDataDTOs, units, lat, lon);
    }

    /**
     * Helper method to generate model and view data fetch from the service
     * layer.
     *
     * @param weatherDataDTOs
     * @return ModelAndView data
     */
    private ModelAndView generateModelAndView(List<WeatherDataDTO> weatherDataDTOs, String units, Double lat, Double lon) {
        WeatherDataDTO currentWeatherDTO = weatherDataDTOs.get(0);
        String timeOfDay = currentWeatherDTO.getFormattedDateTime().contains("am") ? "day" : "night";
        List< WeatherDataDTO> weatherForecastDTO = weatherDataDTOs.subList(1, weatherDataDTOs.size());

        logger.debug("Current weather: {}, Time of day: {}, Forecast count: {}",
                currentWeatherDTO.toString(), timeOfDay, weatherForecastDTO.size());

        ModelAndView modelAndView = new ModelAndView("index"); // Remove/add "-test" after/during testing
        modelAndView.addObject("currentWeather", currentWeatherDTO);
        modelAndView.addObject("units", units);
        modelAndView.addObject("timeOfDay", timeOfDay);
        modelAndView.addObject("weatherForecast", weatherForecastDTO);
        modelAndView.addObject("city", currentWeatherDTO.getCityName());
        if (lat != null & lon != null) {
            modelAndView.addObject("lat", lat);
            modelAndView.addObject("lon", lon);
        }

        return modelAndView;
    }
}
