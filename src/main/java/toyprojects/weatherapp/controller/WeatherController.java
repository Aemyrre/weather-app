package toyprojects.weatherapp.controller;

import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import toyprojects.weatherapp.constants.ValidLangauge;
import toyprojects.weatherapp.constants.ValidUnits;
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
    @RequestParam(required=false, defaultValue="metric") String units,
    @RequestParam(required=false, defaultValue="en") String language) {
        units = unitValidator(units);
        language = languageValidator(language);

        WeatherDataDTO getWeatherDataDTO = weatherService.getWeatherByCity(city, units, language);
        return ResponseEntity.ok(getWeatherDataDTO);
    }

    private String unitValidator(String units) {
        String tempUnit = units;
        boolean isValidUnit = Arrays.stream(ValidUnits.values())
            .anyMatch(unit -> unit.name().equalsIgnoreCase(tempUnit));
            
        return !isValidUnit ? "metric" : units;
    }

    private String languageValidator(String language) {
        String tempLang = language;
        boolean isValidUnit = Arrays.stream(ValidLangauge.values())
            .anyMatch(unit -> unit.name().equalsIgnoreCase(tempLang));

        return !isValidUnit ? "en" : language;
    }
    
}
