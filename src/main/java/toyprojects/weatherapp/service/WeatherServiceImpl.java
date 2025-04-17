package toyprojects.weatherapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import toyprojects.weatherapp.entity.WeatherData;
import toyprojects.weatherapp.entity.WeatherDataDTO;
import toyprojects.weatherapp.exception.CityNotFoundException;
import toyprojects.weatherapp.exception.InvalidApiKeyException;
import toyprojects.weatherapp.validation.WeatherParameterValidation;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.baseurl}")
    private String weatherURL;

    @Value("${openweathermap.api.forecast.baseurl}")
    private String forecastURL;

    private final RestTemplate restTemplate;
    private final WeatherParameterValidation validator;

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    public WeatherServiceImpl(RestTemplate restTemplate, WeatherParameterValidation validator) {
        this.restTemplate = restTemplate;
        this.validator = validator;
    }

    /**
     * Fetches current weather data using city name for the controller
     *
     * @param city
     * @param units
     * @param lang
     *
     * @return WeatherDataDTO
     *
     */
    @Override
    @Cacheable(value = "currentWeather", key = "T(java.util.Objects).hash(#city, #units, #lang)", cacheManager = "cacheManager")
    public WeatherDataDTO getCurrentWeatherDataByCity(String city, String units, String lang) {
        validator.validateCity(city);
        units = validator.validateUnitOfMeasurement(units);
        lang = validator.validateLanguage(lang);

        logger.info("Fetching current weather forecast for city: {}", city);
        String url = String.format("%s?q=%s&appid=%s&units=%s&lang=%s", weatherURL, city, apiKey, units, lang);

        return fetchCurrentWeatherData(url);
    }

    @Override
    @Cacheable(value = "currentWeather", key = "T(java.util.Objects).hash(#lat, #lon, #units, #lang)", cacheManager = "cacheManager")
    public WeatherDataDTO getCurrentWeatherDataByCoordinates(double lat, double lon, String units, String lang) {
        validator.validateCoordinates(lat, lon);
        units = validator.validateUnitOfMeasurement(units);
        lang = validator.validateLanguage(lang);

        logger.info("Fetching current weather data for coordinates: lat={}, lon={}", lat, lon);
        String url = String.format("%s?lat=%s&lon=%s9&appid=%s&units=%s&lang=%s", weatherURL, lat, lon, apiKey, units, lang);

        return fetchCurrentWeatherData(url);
    }

    /**
     * Fetches weather forecast data using city name for the controller
     *
     * @param city
     * @param units
     * @param lang
     *
     * @return List<WeatherDataDTO>
     *
     */
    @Override
    @Cacheable(value = "forecastWeather", key = "T(java.util.Objects).hash(#city, #units, #lang)", cacheManager = "cacheManager")
    public List<WeatherDataDTO> getListWeatherForecastByCity(String city, String units, String lang) {
        validator.validateCity(city);
        units = validator.validateUnitOfMeasurement(units);
        lang = validator.validateLanguage(lang);

        logger.info("Fetching weather forecast for city: {}", city);
        String url = String.format("%s?q=%s&appid=%s&units=%s&lang=%s", forecastURL, city, apiKey, units, lang);

        return fetchForecastWeatherData(url);
    }

    /**
     * Fetches weather forecast data using coordinates for the controller
     *
     * @param lat
     * @param lon
     * @param units
     * @param lang
     *
     * @return List<WeatherDataDTO>
     *
     */
    @Override
    @Cacheable(value = "forecastWeather", key = "T(java.util.Objects).hash(#lat, #lon, #units, #lang)", cacheManager = "cacheManager")
    public List<WeatherDataDTO> getListWeatherForecastByCoordinates(double lat, double lon, String units, String lang) {
        validator.validateCoordinates(lat, lon);
        units = validator.validateUnitOfMeasurement(units);
        lang = validator.validateLanguage(lang);

        logger.info("Fetching weather data for coordinates: lat={}, lon={}", lat, lon);
        String url = String.format("%s?lat=%s&lon=%s9&appid=%s&units=%s&lang=%s", forecastURL, lat, lon, apiKey, units, lang);

        return fetchForecastWeatherData(url);
    }

    /**
     * Helper method to parse json string of current weather data
     *
     * @param url
     * @return WeatherDataDTO
     */
    private WeatherDataDTO fetchCurrentWeatherData(String url) {
        try {
            String jsonRequest = restTemplate.getForObject(url, String.class);
            DocumentContext documentContextWeather = JsonPath.parse(jsonRequest);

            logger.debug("JSON response received: {}", jsonRequest);
            String cityName = documentContextWeather.read("$.name");
            String country = documentContextWeather.read("$.sys.country");
            Integer timezone = documentContextWeather.read("$.timezone");
            Integer dt = documentContextWeather.read("$.dt");
            Long unix = dt.longValue();

            WeatherData weatherData = createWeatherData(documentContextWeather, cityName, country, unix, timezone);
            logger.debug("Current weather data object: {}", weatherData);

            return new WeatherDataDTO(weatherData);

        } catch (HttpClientErrorException.NotFound ex) {
            logger.error("Coordinates not found: url={}", url, ex);
            throw new CityNotFoundException();
        } catch (HttpClientErrorException.Unauthorized ex) {
            logger.error("Invalid API key used for coordinates: url={}", url, ex);
            throw new InvalidApiKeyException();
        } catch (RestClientException ex) {
            logger.error("Error fetching weather data for URL: {} with exception: {}", url, ex.getMessage(), ex);
            throw new RuntimeException("Error fetching weather data.", ex);
        }
    }

    /**
     * Helper method for fetching weather forecast data
     *
     * @param url
     * @return List<WeatherDataDTO>
     */
    private List<WeatherDataDTO> fetchForecastWeatherData(String url) {
        try {
            String jsonRequest = restTemplate.getForObject(url, String.class);
            DocumentContext documentContext = JsonPath.parse(jsonRequest);

            logger.debug("JSON response received: {}", jsonRequest);
            List<WeatherData> weatherDataList = parseWeatherDataList(documentContext);

            for (int i = 0; i < weatherDataList.size(); i++) {
                logger.debug("Weather data object No. {}: {}", i, weatherDataList.get(i));
            }

            return weatherDataList.stream()
                    .map(WeatherDataDTO::new)
                    .collect(Collectors.toList());

        } catch (HttpClientErrorException.NotFound ex) {
            logger.error("Coordinates not found: url={}", url, ex);
            throw new CityNotFoundException();
        } catch (HttpClientErrorException.Unauthorized ex) {
            logger.error("Invalid API key used for coordinates: url={}", url, ex);
            throw new InvalidApiKeyException();
        } catch (RestClientException ex) {
            logger.error("Error fetching weather data for URL: {} with exception: {}", url, ex.getMessage(), ex);
            throw new RuntimeException("Error fetching weather data.", ex);
        }
    }

    /**
     * Helper method to parse json string of weather forecasts and turn them
     * into an List weather data objects
     *
     * @param documentContext
     * @return
     */
    private List<WeatherData> parseWeatherDataList(DocumentContext documentContext) {
        List<WeatherData> weatherDataList = new ArrayList<>();
        String cityName = documentContext.read("$.city.name");
        String country = documentContext.read("$.city.country");
        Integer timezone = documentContext.read("$.city.timezone");

        List<Object> weatherList = documentContext.read("$.list[*]");

        for (int i = 0; i < Math.min(10, weatherList.size()); i++) {
            DocumentContext documentContextWeather = JsonPath.parse(weatherList.get(i));
            Integer dt = documentContextWeather.read("$.dt");
            Long unix = dt.longValue();
            weatherDataList.add(createWeatherData(documentContextWeather, cityName, country, unix, timezone));
        }

        return weatherDataList;
    }

    /**
     * Helper method to create WeatherData object
     *
     * @param documentContext
     * @param cityName
     * @param country
     * @param unix
     * @param timezone
     * @return
     */
    private WeatherData createWeatherData(DocumentContext documentContext, String cityName, String country, Long unix, Integer timezone) {
        if (unix == null) {
            Integer unixInt = documentContext.read("$.dt");
            unix = unixInt.longValue();
        }

        return new WeatherData(
                cityName != null ? cityName : documentContext.read("$.name"),
                country != null ? country : documentContext.read("$.sys.country"),
                documentContext.read("$.weather[0].id"),
                documentContext.read("$.weather[0].main"),
                documentContext.read("$.weather[0].description"),
                documentContext.read("$.weather[0].icon"),
                ((Number) documentContext.read("$.main.temp")).doubleValue(),
                ((Number) documentContext.read("$.main.feels_like")).doubleValue(),
                ((Number) documentContext.read("$.main.temp_min")).doubleValue(),
                ((Number) documentContext.read("$.main.temp_max")).doubleValue(),
                documentContext.read("$.main.humidity"),
                ((Number) documentContext.read("$.wind.speed")).doubleValue(),
                unix,
                timezone != null ? timezone : documentContext.read("$.timezone")
        );
    }
}
