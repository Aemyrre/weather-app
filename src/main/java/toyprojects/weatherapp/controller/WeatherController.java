package toyprojects.weatherapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import toyprojects.weatherapp.entity.WeatherDataDTO;
import toyprojects.weatherapp.exception.CityNotFoundException;
import toyprojects.weatherapp.exception.ErrorResponse;
import toyprojects.weatherapp.service.WeatherService;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * GET method for getting weather data by city. Returns a weather object DTO
     * in json
     *
     * @param city
     * @return
     */
    @GetMapping("/{city}")
    public ResponseEntity<WeatherDataDTO> getWeatherByCity(@PathVariable String city) {
        WeatherDataDTO getWeatherDataDTO = weatherService.getWeatherByCity(city);
        return ResponseEntity.ok(getWeatherDataDTO);
    }

    /**
     * GET method for getting weather data by city, unit of measurement, and
     * language. Refer to `constants/` folder for details regarding unit of
     * measurement and language.
     *
     * @param city
     * @param units
     * @param lang
     * @return
     */
    @GetMapping
    public ResponseEntity<WeatherDataDTO> getWeatherByCity(@RequestParam(required = false) String city,
            @RequestParam(required = false, defaultValue = "metric") String units,
            @RequestParam(required = false, defaultValue = "en") String lang) {

        if (city == null || city.trim().isEmpty()) {
            handleEmptyCity();
        }

        WeatherDataDTO getWeatherDataDTO = weatherService.getWeatherByCity(city, units, lang);
        return ResponseEntity.ok(getWeatherDataDTO);
    }

    /**
     * GET method for getting weather data by city. Returns a weather forecast
     * DTO by city in json. page and size are for pagination; however, they are
     * not required.
     *
     * @param city
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/forecast/{city}")
    public ResponseEntity<Page<WeatherDataDTO>> getSortedWeather3Hr5dayForecastByCity(@PathVariable String city,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "8") int size) {

        Page<WeatherDataDTO> weatherDataDTOs = weatherService.getSortedWeather3Hr5dayForecastByCity(city, page, size);

        return ResponseEntity.ok(weatherDataDTOs);
    }

    /**
     * GET method for getting weather forecast DTOs by city, unit of
     * measurement, and language in json. Refer to `constants/` folder for
     * details regarding unit of measurement and language. Except for city, all
     * paramaters are optional.
     *
     * @param city
     * @param units
     * @param lang
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/forecast")
    public ResponseEntity<Page<WeatherDataDTO>> getSortedWeather3Hr5dayForecastByCity(@PathVariable(required = false) String city,
            @RequestParam(required = false, defaultValue = "metric") String units,
            @RequestParam(required = false, defaultValue = "en") String lang,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "8") int size) {

        if (city == null || city.trim().isEmpty()) {
            handleEmptyCity();
        }

        Page<WeatherDataDTO> weatherDataDTOs = weatherService.getSortedWeather3Hr5dayForecastByCity(city, page, size);

        return ResponseEntity.ok(weatherDataDTOs);
    }

    /**
     * GET method when passing an empty city argument.
     *
     * @return
     */
    @GetMapping({"/forecast", "/forecast/", "/"})
    public ResponseEntity<ErrorResponse> handleEmptyCity() {
        throw new CityNotFoundException("City must be provided");
    }

    // @Deprecated
    // @GetMapping("/forecast/{city}")
    // public ResponseEntity<List<WeatherDataDTO>> getWeather3HourForecastByCity(@RequestParam String city) {
    //     List<WeatherDataDTO> weatherDataDTOs = weatherService.getListWeather3Hr5dayForecastByCity(city);
    //     return ResponseEntity.ok(weatherDataDTOs);
    // }
    // @Deprecated
    // @GetMapping("/forecast")
    // public ResponseEntity<List<WeatherDataDTO>> getWeather3HourForecastByCity(@RequestParam String city,
    //         @RequestParam(required = false, defaultValue = "metric") String units,
    //         @RequestParam(required = false, defaultValue = "en") String lang) {
    //     List<WeatherDataDTO> weatherDataDTOs = new ArrayList<>();
    //     return ResponseEntity.ok(weatherDataDTOs);
    // }
}
