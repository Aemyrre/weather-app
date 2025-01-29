package toyprojects.weatherapp.service;

import toyprojects.weatherapp.entity.WeatherDataDTO;

public interface WeatherService {
    WeatherDataDTO getWeatherByCity(String city);
    WeatherDataDTO getWeatherByCity(String city, String units, String language);
}
