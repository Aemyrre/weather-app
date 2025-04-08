package toyprojects.weatherapp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        for (Object weather : weatherList) {
            DocumentContext documentContextWeather = JsonPath.parse(weather);
            Integer dt = JsonPath.parse(weather).read("$.dt");
            Long unix = dt.longValue();
            weatherDataList.add(createWeatherData(documentContextWeather, cityName, country, unix, timezone));
        }

        return weatherDataList;
    }

    /**
     * Prints weather data object for testing
     *
     * @param weatherData
     */
    private void printWeatherData(WeatherData weatherData) {
        System.out.println("City: " + weatherData.getCityName());
        System.out.println("Country: " + weatherData.getCountry());
        System.out.println("Temperature: " + weatherData.getTemperature());
        System.out.println("Main Weather Decsription: " + weatherData.getWeatherMainDescription());
        System.out.println("Weather Description: " + weatherData.getWeatherDescription());
        System.out.println("Wind Speed: " + weatherData.getWindSpeed());
        System.out.println("As of Local Time: " + weatherData.getFormattedDateTime());
    }

    /**
     * Method for getting weather data using city as parameter. The method
     * default unit and language values are metric and english, respectively.
     */
    @Override
    public WeatherDataDTO getWeatherByCity(String city) {
        return getWeatherByCity(city, null, null);
    }

    /**
     * Method for getting weather data using multiple parameters to retrive
     * weather data. In adition to the city, users can choose imperial, metric,
     * and openweathermap api standard unit; please see
     * constants/ValidUnits.java for details. Method also accepts different
     * languages to choose from as parameter Please see
     * constants/ValidLanguage.java for details
     *
     */
    @Override
    public WeatherDataDTO getWeatherByCity(String city, String units, String lang) {

        validator.validateCity(city);
        units = validator.validateUnitOfMeasurement(units);
        lang = validator.validateLanguage(lang);

        try {
            String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=%s&lang=%s", city, apiKey, units, lang);
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

    /**
     * Method for getting weather data using lattitude and longitude as
     * parameters. The method default unit and language values are metric and
     * english, respectively.
     */
    @Override
    public WeatherDataDTO getWeatherByCoordinates(double lat, double lon) {
        return getWeatherByCoordinates(lat, lon, null, null);
    }

    /**
     * Method for getting weather data using multiple parameters to retrive
     * weather data. In adition to lattitude and longitude, users can choose
     * imperial, metric, and openweathermap api standard unit; please see
     * constants/ValidUnits.java for details. Method also accepts different
     * languages to choose from as parameter Please see
     * constants/ValidLanguage.java for details
     *
     */
    @Override
    public WeatherDataDTO getWeatherByCoordinates(double lat, double lon, String units, String lang) {
        validator.validateCoordinates(lat, lon);

        try {
            String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=%s&lang=%s", lat, lon, apiKey, units, lang);
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

    /**
     * Deprecated. Method for calling 3hr/5days weather forecast (total of 40
     * weather data objects with 3-hour interval). The method default unit and
     * language values are metric and english, respectively.
     *
     */
    @Deprecated
    @Override
    public List<WeatherDataDTO> getListWeather3Hr5dayForecastByCity(String city) {
        return getListWeather3Hr5dayForecastByCity(city, null, null);
    }

    /**
     * Deprecated. Method for calling 3hr/5days weather forecast (total of 40
     * weather data objects with 3-hour interval). In adition to the city, users
     * can choose imperial, metric, and openweathermap api standard unit; please
     * see constants/ValidUnits.java for details. Method also accepts different
     * languages to choose from as parameter Please see
     * constants/ValidLanguage.java for details
     *
     */
    @Deprecated
    @Override
    public List<WeatherDataDTO> getListWeather3Hr5dayForecastByCity(String city, String units, String lang) {

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

    /**
     * Method to replace `getWeather3HourForecastByCity(String city)` Return a
     * paginated weather forecast. The method default unit and language values
     * are metric and english, respectively.
     *
     */
    @Override
    public Page<WeatherDataDTO> getSortedWeather3Hr5dayForecastByCity(String city, int page, int size) {
        return getSortedWeather3Hr5dayForecastByCity(city, null, null, page, size);
    }

    /**
     * Method to replace `getWeather3HourForecastByCity(String city, String
     * units, String lang)` Return a paginated weather forecast. The method
     * default unit and language values are metric and english, respectively.
     *
     */
    @Override
    public Page<WeatherDataDTO> getSortedWeather3Hr5dayForecastByCity(String city, String units, String lang, int page, int size) {
        List<WeatherDataDTO> sortedWeatherDataDTOs = getListWeather3Hr5dayForecastByCity(city, units, lang).stream()
                .sorted(Comparator.comparing(WeatherDataDTO::getFormattedDateTime))
                .collect(Collectors.toList());

        int pageSize = size >= 1 ? size : 8;
        int currentPage = page >= 0 ? page : 0;
        int startItem = currentPage * pageSize;

        List<WeatherDataDTO> pagedData;

        if (sortedWeatherDataDTOs.size() < startItem) {
            pagedData = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, sortedWeatherDataDTOs.size());
            pagedData = sortedWeatherDataDTOs.subList(startItem, toIndex);
        }

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        return new PageImpl<>(pagedData, pageable, sortedWeatherDataDTOs.size());
    }

}
