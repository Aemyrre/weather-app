package toyprojects.weatherapp.service;

import java.util.List;

import toyprojects.weatherapp.entity.WeatherDataDTO;

public interface WeatherService {

    WeatherDataDTO getCurrentWeatherDataByCity(String city, String units, String lang);

    WeatherDataDTO getCurrentWeatherDataByCoordinates(double lat, double lon, String units, String lang);

    WeatherDataDTO getCachedCurrentWeatherDataByCity(String city, String units, String lang);

    WeatherDataDTO getCachedCurrentWeatherDataByCoordinates(double lat, double lon, String units, String lang);

    List<WeatherDataDTO> getListWeatherForecastByCity(String city, String units, String lang);

    List<WeatherDataDTO> getListWeatherForecastByCoordinates(double lat, double lon, String units, String lang);

}
