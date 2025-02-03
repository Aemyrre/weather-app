package toyprojects.weatherapp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private WeatherData createWeatherData(DocumentContext documentContext, String cityName, String country, Long unix, Integer timezone) {
        if (unix == null) {
            Integer unixInt = documentContext.read("$.dt");
            unix = unixInt.longValue();
        }

        return new WeatherData(
                cityName != null ? cityName : documentContext.read("$.name"),
                country != null ? country : documentContext.read("$.sys.country"),
                documentContext.read("$.weather[0].id"),
                documentContext.read("$.weather[0].description"),
                documentContext.read("$.weather[0].icon"),
                new BigDecimal(documentContext.read("$.main.temp").toString()),
                new BigDecimal(documentContext.read("$.main.feels_like").toString()),
                new BigDecimal(documentContext.read("$.main.temp_min").toString()),
                new BigDecimal(documentContext.read("$.main.temp_max").toString()),
                documentContext.read("$.main.humidity"),
                new BigDecimal(documentContext.read("$.wind.speed").toString()),
                unix,
                timezone != null ? timezone : documentContext.read("$.timezone")
        );
    }

    private List<WeatherData> parseWeatherDataList(DocumentContext documentContext) {
        List<WeatherData> weatherDataList = new ArrayList<>();
        String cityName = documentContext.read("$.city.name");
        String country = documentContext.read("$.city.country");
        Integer timezone = documentContext.read("$.city.timezone");

        List<Object> weatherList = documentContext.read("$.list[*]");

        for (Object weather : weatherList) {
            DocumentContext documentContextWeather = JsonPath.parse(weather);
            Integer dt = JsonPath.parse(weather).read("$.dt");
            Long unix = dt.longValue();
            weatherDataList.add(createWeatherData(documentContextWeather, cityName, country, unix, timezone));
        }

        return weatherDataList;
    }

    private void printWeatherData(WeatherData weatherData) {
        System.out.println("City: " + weatherData.getCityName());
        System.out.println("Country: " + weatherData.getCountry());
        System.out.println("Temperature: " + weatherData.getTemperature());
        System.out.println("Weather Description: " + weatherData.getWeatherDescription());
        System.out.println("Wind Speed: " + weatherData.getWindSpeed());
        System.out.println("As of Local Time: " + weatherData.getFormattedDateTime());
    }

    @Override
    public WeatherDataDTO getWeatherByCity(String city) {
        return getWeatherByCity(city, null, null);
    }

    @Override
    public WeatherDataDTO getWeatherByCity(String city, String units, String lang) {

        validator.validateCity(city);
        units = validator.validateUnitOfMeasurement(units);
        lang = validator.validateLanguage(lang);

        try {
            String url = String.format("%s%s%s%s%s%s%s%s", "https://api.openweathermap.org/data/2.5/weather?q=", city, "&appid=", apiKey, "&units=", units, "&lang=", lang);
            String jsonRequest = restTemplate.getForObject(url, String.class);
            DocumentContext documentContext = JsonPath.parse(jsonRequest);

            System.out.println(jsonRequest);
            WeatherData weatherData = createWeatherData(documentContext, null, null, null, null);

            printWeatherData(weatherData);

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
    public List<WeatherDataDTO> getWeather3HourForecastByCity(String city) {
        return getWeather3HourForecastByCity(city, null, null);
    }

    @Override
    public List<WeatherDataDTO> getWeather3HourForecastByCity(String city, String units, String lang) {

        validator.validateCity(city);
        units = validator.validateUnitOfMeasurement(units);
        lang = validator.validateLanguage(lang);

        try {
            String url = String.format("%s%s%s%s%s%s%s%s", "https://api.openweathermap.org/data/2.5/forecast?q=", city, "&appid=", apiKey, "&units=", units, "&lang=", lang);
            String jsonRequest = restTemplate.getForObject(url, String.class);
            DocumentContext documentContext = JsonPath.parse(jsonRequest);

            System.out.println(jsonRequest);
            List<WeatherData> weatherDataList = parseWeatherDataList(documentContext);

            int i = 0;
            for (WeatherData weatherData : weatherDataList) {
                System.out.println("No." + i++);
                printWeatherData(weatherData);
                System.out.println("-------------------");
            }

            return weatherDataList.stream()
                    .map(WeatherDataDTO::new)
                    .collect(Collectors.toList());

        } catch (HttpClientErrorException.NotFound ex) {
            throw new CityNotFoundException();
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new InvalidApiKeyException();
        } catch (RestClientException ex) {
            throw new RuntimeException("Error fetching weather data.", ex);
        }
    }

}
