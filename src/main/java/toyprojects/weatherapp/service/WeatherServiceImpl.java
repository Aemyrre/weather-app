package toyprojects.weatherapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import io.github.bucket4j.Bucket;
import io.github.cdimascio.dotenv.Dotenv;
import toyprojects.weatherapp.entity.WeatherData;
import toyprojects.weatherapp.entity.WeatherDataDTO;
import toyprojects.weatherapp.exception.CityNotFoundException;
import toyprojects.weatherapp.exception.InvalidApiKeyException;
import toyprojects.weatherapp.exception.RateLimitExceededException;
import toyprojects.weatherapp.validation.WeatherParameterValidation;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final String API_KEY = dotenv.get("API_KEY", System.getenv("API_KEY"));

    @Value("${openweathermap.api.baseurl}")
    private String weatherURL;

    @Value("${openweathermap.api.forecast.baseurl}")
    private String forecastURL;

    private final RestTemplate restTemplate;
    private final WeatherParameterValidation validator;

    private final CacheManager cacheManager;

    private final RateLimiterService rateLimiterService;

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    public WeatherServiceImpl(RestTemplate restTemplate, WeatherParameterValidation validator, CacheManager cacheManager, RateLimiterService rateLimiterService) {
        this.restTemplate = restTemplate;
        this.validator = validator;
        this.cacheManager = cacheManager;
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public WeatherDataDTO getWeatherDataByCityWithRateLimit(String clientIp, String city, String units, String lang) {

        Bucket bucket = rateLimiterService.getBucket(clientIp);
        WeatherDataDTO cachedWeatherDataDTO = getCachedCurrentWeatherDataByCity(city, units, lang);
        if (cachedWeatherDataDTO == null) {
            if (!bucket.tryConsume(1)) {
                logger.warn("Rate limit exceeded for IP: {}", clientIp);
                throw new RateLimitExceededException();
            }

            logger.info("Weather retrieved from API. City={}", city);
            return getCurrentWeatherDataByCity(city, units, lang);
        }

        logger.info("Weather retrieved from cache. City={}", city);

        logger.info("Rate limiting applied only for API requests, not cached data.");
        logger.info("Client IP: {}", clientIp);
        logger.info("Remaining rate limit: {}", bucket.getAvailableTokens());
        return cachedWeatherDataDTO;
    }

    @Override
    public WeatherDataDTO getWeatherDataByCoordinatesWithRateLimit(String clientIp, double lat, double lon,
            String units, String lang) {

        Bucket bucket = rateLimiterService.getBucket(clientIp);
        WeatherDataDTO cachedWeatherDataDTO = getCachedCurrentWeatherDataByCoordinates(lat, lon, units, lang);

        if (cachedWeatherDataDTO == null) {
            if (!bucket.tryConsume(1)) {
                logger.warn("Rate limit exceeded for IP: {}", clientIp);
                throw new RateLimitExceededException();
            }

            logger.info("Weather retrieved from API. lat={}, lon={}", lat, lon);
            return getCurrentWeatherDataByCoordinates(lat, lon, units, lang);
        }

        logger.info("Weather retrieved from cache. lat={}, lon={}", lat, lon);

        logger.info("Rate limiting applied only for API requests, not cached data.");
        logger.info("Client IP: {}", clientIp);
        logger.info("Remaining rate limit: {}", bucket.getAvailableTokens());
        return cachedWeatherDataDTO;
    }

    /**
     * Fetches current weather data using city name.
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
        String url = String.format("%s?q=%s&appid=%s&units=%s&lang=%s", weatherURL, city, API_KEY, units, lang);

        return fetchCurrentWeatherData(url);
    }

    /**
     * Fetches current weather data using coordinates (latitude, longitude)
     *
     * @param lat
     * @param lon
     * @param units
     * @param lang
     *
     * @return WeatherDataDTO
     */
    @Override
    @Cacheable(value = "currentWeather", key = "T(java.util.Objects).hash(#lat, #lon, #units, #lang)", cacheManager = "cacheManager")
    public WeatherDataDTO getCurrentWeatherDataByCoordinates(double lat, double lon, String units, String lang) {
        validator.validateCoordinates(lat, lon);
        units = validator.validateUnitOfMeasurement(units);
        lang = validator.validateLanguage(lang);

        logger.info("Fetching current weather data for coordinates: lat={}, lon={}", lat, lon);
        String url = String.format("%s?lat=%s&lon=%s9&appid=%s&units=%s&lang=%s", weatherURL, lat, lon, API_KEY, units, lang);

        return fetchCurrentWeatherData(url);
    }

    /**
     * Manually fetches cached current weather data using city name
     *
     * @param city
     * @param units
     * @param lang
     *
     * @return WeatherDataDTO
     *
     */
    @Override
    public WeatherDataDTO getCachedCurrentWeatherDataByCity(String city, String units, String lang) {
        Cache cache = cacheManager.getCache("currentWeather");
        if (cache != null) {
            Object cacheKey = java.util.Objects.hash(city, units, lang);
            return cache.get(cacheKey, WeatherDataDTO.class);
        }
        return null;
    }

    /**
     * Manually fetches cached current weather data using coordinates (latitude,
     * longitude)
     *
     * @param lat
     * @param lon
     * @param units
     * @param lang
     *
     * @return WeatherDataDTO
     */
    @Override
    public WeatherDataDTO getCachedCurrentWeatherDataByCoordinates(double lat, double lon, String units, String lang) {
        Cache cache = cacheManager.getCache("currentWeather");
        if (cache != null) {
            Object cacheKey = java.util.Objects.hash(lat, lon, units, lang);
            return cache.get(cacheKey, WeatherDataDTO.class);
        }
        return null;
    }

    /**
     * Fetches weather forecast data using city name
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
        String url = String.format("%s?q=%s&appid=%s&units=%s&lang=%s", forecastURL, city, API_KEY, units, lang);

        return fetchForecastWeatherData(url);
    }

    /**
     * Fetches weather forecast data using coordinates
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
        String url = String.format("%s?lat=%s&lon=%s9&appid=%s&units=%s&lang=%s", forecastURL, lat, lon, API_KEY, units, lang);

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
            Integer dtSunrise = documentContextWeather.read("$.sys.sunrise");
            Integer dtSunset = documentContextWeather.read("$.sys.sunset");

            Long unix = dt.longValue();
            Long sunrise = dtSunrise.longValue();
            Long sunset = dtSunset.longValue();

            WeatherData weatherData = createWeatherData(documentContextWeather, cityName, country, unix, timezone, sunrise, sunset);
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

            weatherDataList.add(createWeatherData(documentContextWeather, cityName, country, unix, timezone, null, null));
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
    private WeatherData createWeatherData(DocumentContext documentContext, String cityName, String country, Long unix, Integer timezone, Long sunrise, Long sunset) {
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
                timezone != null ? timezone : documentContext.read("$.timezone"),
                sunrise,
                sunset
        );
    }

}
