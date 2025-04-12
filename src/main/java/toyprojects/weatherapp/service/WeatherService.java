package toyprojects.weatherapp.service;

import java.util.List;

import toyprojects.weatherapp.entity.WeatherDataDTO;

public interface WeatherService {

    List<WeatherDataDTO> getListWeatherForecastByCity(String city, String units, String lang);

    List<WeatherDataDTO> getListWeatherForecastByCoordinates(double lat, double lon, String units, String lang);

}
