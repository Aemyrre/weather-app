package toyprojects.weatherapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import toyprojects.weatherapp.entity.WeatherDataDTO;
import toyprojects.weatherapp.service.WeatherService;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public ResponseEntity<WeatherDataDTO> getWeatherByCity(@PathVariable String city) {
        WeatherDataDTO getWeatherDataDTO = weatherService.getWeatherByCity(city);
        return ResponseEntity.ok(getWeatherDataDTO);
    }

    @GetMapping
    public ResponseEntity<WeatherDataDTO> getWeatherByCity(@RequestParam String city,
            @RequestParam(required = false, defaultValue = "metric") String units,
            @RequestParam(required = false, defaultValue = "en") String lang) {

        WeatherDataDTO getWeatherDataDTO = weatherService.getWeatherByCity(city, units, lang);
        return ResponseEntity.ok(getWeatherDataDTO);
    }

    @GetMapping("/forecast/{city}")
    public ResponseEntity<List<WeatherDataDTO>> getWeather3HourForecastByCity(@RequestParam String city) {
        List<WeatherDataDTO> weatherDataDTOs = weatherService.getWeather3HourForecastByCity(city);
        return ResponseEntity.ok(weatherDataDTOs);
    }

    @GetMapping("/forecast")
    public ResponseEntity<List<WeatherDataDTO>> getWeather3HourForecastByCity(@RequestParam String city,
            @RequestParam(required = false, defaultValue = "metric") String units,
            @RequestParam(required = false, defaultValue = "en") String lang) {
        List<WeatherDataDTO> weatherDataDTOs = new ArrayList<>();
        return ResponseEntity.ok(weatherDataDTOs);
    }

}
