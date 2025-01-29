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

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherDataDTO getWeatherByCity(String city) {
        try {
            String url = String.format("%s%s%s%s%s", "https://api.openweathermap.org/data/2.5/weather?q=", city, "&appid=", apiKey, "&units=metric");
            String jsonRequest = restTemplate.getForObject(url, String.class);
            System.out.println(jsonRequest);

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

            WeatherData weatherData = new WeatherData(cityName, country, weatherId, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed);

            return new WeatherDataDTO(weatherData);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new CityNotFoundException("City not found");
        } catch (RestClientException ex) {
            throw new RuntimeException("Error fetching weather data.", ex);
        }
    }

    @Override
    public WeatherDataDTO getWeatherByCity(String city, String units, String language) {
        try {
            String url = String.format("%s%s%s%s%s%s%s%s", "https://api.openweathermap.org/data/2.5/weather?q=", city, "&appid=", apiKey, "&units=", units, "&lang=", language);
            String jsonRequest = restTemplate.getForObject(url, String.class);
            System.out.println(jsonRequest);

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

            WeatherData weatherData = new WeatherData(cityName, country, weatherId, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed);

            return new WeatherDataDTO(weatherData);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new CityNotFoundException(ex.getMessage());
        } catch (RestClientException ex) {
            throw new RuntimeException("Error fetching weather data.", ex);
        }
    }
}
