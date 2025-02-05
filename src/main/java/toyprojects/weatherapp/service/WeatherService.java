package toyprojects.weatherapp.service;

import java.util.List;

import org.springframework.data.domain.Page;

import toyprojects.weatherapp.entity.WeatherDataDTO;

public interface WeatherService {

    WeatherDataDTO getWeatherByCity(String city);

    WeatherDataDTO getWeatherByCity(String city, String units, String lang);

    @Deprecated
    List<WeatherDataDTO> getListWeather3Hr5dayForecastByCity(String city);

    @Deprecated
    List<WeatherDataDTO> getListWeather3Hr5dayForecastByCity(String city, String units, String lang);

    Page<WeatherDataDTO> getSortedWeather3Hr5dayForecastByCity(String city, int page, int size);

    Page<WeatherDataDTO> getSortedWeather3Hr5dayForecastByCity(String city, String units, String lang, int page, int size);
}
