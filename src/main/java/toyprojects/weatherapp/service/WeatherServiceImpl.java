package toyprojects.weatherapp.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
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

    private final RestTemplate restTemplate;
    private final WeatherParameterValidation validator;

    public WeatherServiceImpl(RestTemplate restTemplate, WeatherParameterValidation validator) {
        this.restTemplate = restTemplate;
        this.validator = validator;
    }

    @Override
    public WeatherDataDTO getWeatherByCity(String city) {
        validator.validateCity(city);

        try {
            String url = String.format("%s%s%s%s%s", "https://api.openweathermap.org/data/2.5/weather?q=", city, "&appid=", apiKey, "&units=metric");
            String jsonRequest = restTemplate.getForObject(url, String.class);
            System.out.println(jsonRequest);

            WeatherData weatherData = createWeatherData(jsonRequest);

            return new WeatherDataDTO(weatherData);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new CityNotFoundException();
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new InvalidApiKeyException();
        } catch (RestClientException ex) {
            throw new RuntimeException("Error fetching weather data.", ex);
        }
    }

    @Override
    public WeatherDataDTO getWeatherByCity(String city, String units, String lang) {

        validator.validateCity(city);
        units = validator.validateUnitOfMeasurement(units);
        lang = validator.validateLanguage(lang);

        try {
            String url = String.format("%s%s%s%s%s%s%s%s", "https://api.openweathermap.org/data/2.5/weather?q=", city, "&appid=", apiKey, "&units=", units, "&lang=", lang);
            String jsonRequest = restTemplate.getForObject(url, String.class);
            System.out.println(jsonRequest);

            WeatherData weatherData = createWeatherData(jsonRequest);

            return new WeatherDataDTO(weatherData);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new CityNotFoundException();
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new InvalidApiKeyException();
        } catch (RestClientException ex) {
            throw new RuntimeException("Error fetching weather data.", ex);
        }
    }

    private WeatherData createWeatherData(String jsonRequest) {
        DocumentContext documentContext = JsonPath.parse(jsonRequest);
        String cityName = documentContext.read("$.name");
        String country = documentContext.read("$.sys.country");
        int weatherId = documentContext.read("$.weather[0].id");
        String weatherDescription = documentContext.read("$.weather[0].description");
        String weatherIcon = documentContext.read("$.weather[0].icon");
        BigDecimal temperature = new BigDecimal(documentContext.read("$.main.temp").toString());
        BigDecimal tempFeelsLike = new BigDecimal(documentContext.read("$.main.feels_like").toString());
        BigDecimal minTemp = new BigDecimal(documentContext.read("$.main.temp_min").toString());
        BigDecimal maxTemp = new BigDecimal(documentContext.read("$.main.temp_max").toString());
        BigDecimal windSpeed = new BigDecimal(documentContext.read("$.wind.speed").toString());
        int humidity = documentContext.read("$.main.humidity");

        return new WeatherData(cityName, country, weatherId, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed);
    }
}
