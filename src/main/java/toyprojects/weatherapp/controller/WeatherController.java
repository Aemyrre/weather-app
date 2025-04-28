package toyprojects.weatherapp.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import toyprojects.weatherapp.entity.WeatherDataDTO;
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
     * @return weather data, time of day and forecasts data to "weather.html"
     */
    @GetMapping("/search")
    public ModelAndView getListWeatherForecastByCity(@RequestParam String city,
            @RequestParam(required = false, defaultValue = "metric") String units,
            @RequestParam(required = false, defaultValue = "en") String lang,
            HttpServletRequest request) {

        String clientIp = request.getRemoteAddr();

        WeatherDataDTO currentWeatherDataDTO = weatherService.getWeatherDataByCityWithRateLimit(clientIp, city, units, lang);
        List<WeatherDataDTO> forecastWeatherDataDTO = weatherService.getListWeatherForecastByCity(city, units, lang);

        return generateModelAndView(currentWeatherDataDTO, forecastWeatherDataDTO, units, null, null);
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
     * @return weather data, time of day and forecasts data to "weather.html"
     */
    @GetMapping
    public ModelAndView getListWeatherForecastByCoordinates(@RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            @RequestParam(required = false, defaultValue = "metric") String units,
            @RequestParam(required = false, defaultValue = "en") String lang,
            HttpServletRequest request) {

        String clientIp = request.getRemoteAddr();

        if (lat == null || lon == null) {
            logger.warn("Latitude or longitude missing. Returning 'index.html'");
            ModelAndView modelAndView = new ModelAndView("index");
            return modelAndView;
        }

        WeatherDataDTO currentWeatherDataDTO = weatherService.getWeatherDataByCoordinatesWithRateLimit(clientIp, lat, lon, units, lang);
        List<WeatherDataDTO> forecastWeatherDataDTO = weatherService.getListWeatherForecastByCoordinates(lat, lon, units, lang);

        return generateModelAndView(currentWeatherDataDTO, forecastWeatherDataDTO, units, lat, lon);
    }

    /**
     * Helper method to generate model and view data fetch from the service
     * layer.
     *
     * @param weatherDataDTOs
     * @return ModelAndView data
     */
    private ModelAndView generateModelAndView(WeatherDataDTO currentWeatherDataDTO, List<WeatherDataDTO> forecastWeatherDataDTO, String units, Double lat, Double lon) {
        String timeOfDay = generateTimeOfDay(currentWeatherDataDTO.getFormattedDateTime());

        logger.debug("Current weather: {}, Time of day: {}, Forecast count: {}",
                currentWeatherDataDTO.toString(), timeOfDay, forecastWeatherDataDTO.size());

        ModelAndView modelAndView = new ModelAndView("weather");
        modelAndView.addObject("currentWeather", currentWeatherDataDTO);
        modelAndView.addObject("units", units);
        modelAndView.addObject("timeOfDay", timeOfDay);
        modelAndView.addObject("weatherForecast", forecastWeatherDataDTO);
        modelAndView.addObject("city", currentWeatherDataDTO.getCityName());
        if (lat != null && lon != null) {
            modelAndView.addObject("lat", lat);
            modelAndView.addObject("lon", lon);
        }

        return modelAndView;
    }

    /**
     * Helper method to determine time of day
     *
     * @param time
     * @return "day" or "night"
     */
    private String generateTimeOfDay(String time) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
            LocalTime localTime = LocalTime.parse(time, formatter);

            logger.debug("Parsed time: {}, hours: {}", localTime, localTime.getHour());

            int hours = localTime.getHour();
            if (hours >= 6 && hours < 18) {
                return "day";
            }
            return "night";
        } catch (DateTimeParseException ex) {
            logger.error("Error parsing time: {}", time, ex);
            return "day";
        }
    }
}
