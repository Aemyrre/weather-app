package toyprojects.weatherapp.service;

import java.util.List;

import toyprojects.weatherapp.entity.WeatherDataDTO;

public interface WeatherService {

    WeatherDataDTO getWeatherByCity(String city);

    WeatherDataDTO getWeatherByCity(String city, String units, String language);

    List<WeatherDataDTO> getWeather3HourForecastByCity(String city);

    List<WeatherDataDTO> getWeather3HourForecastByCity(String city, String units, String language);
}
