package toyprojects.weatherapp.service;

import java.util.List;

import toyprojects.weatherapp.entity.WeatherDataDTO;

public interface WeatherService {

    WeatherDataDTO getWeatherDataByCityWithRateLimit(String clientIp, String city, String units, String lang);

    WeatherDataDTO getWeatherDataByCoordinatesWithRateLimit(String clientIp, double lat, double lon, String units, String lang);

    WeatherDataDTO getCurrentWeatherDataByCity(String city, String units, String lang);

    WeatherDataDTO getCurrentWeatherDataByCoordinates(double lat, double lon, String units, String lang);

    /**
     * Retrieves cached current weather data for a city. If cache is not
     * available, calls external API.
     */
    WeatherDataDTO getCachedCurrentWeatherDataByCity(String city, String units, String lang);

    /**
     * Retrieves cached current weather data for coordinates. If cache is not
     * available, calls external API.
     */
    WeatherDataDTO getCachedCurrentWeatherDataByCoordinates(double lat, double lon, String units, String lang);

    List<WeatherDataDTO> getListWeatherForecastByCity(String city, String units, String lang);

    List<WeatherDataDTO> getListWeatherForecastByCoordinates(double lat, double lon, String units, String lang);

}
