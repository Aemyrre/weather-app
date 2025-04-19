package toyprojects.weatherapp.entity;

import java.util.Objects;

public class WeatherDataDTO {

    private String cityName;
    private String country;

    private int weatherId;
    private String weatherMainDescription;
    private String weatherDescription;
    private String weatherIcon;

    private int temperature;
    private int tempFeelsLike;
    private int minTemp;
    private int maxTemp;
    private int humidity;

    private int windSpeed;

    private String sunrise;
    private String sunset;

    private String formattedDateTime;

    public WeatherDataDTO() {
    }

    public WeatherDataDTO(WeatherData source) {
        this.cityName = source.getCityName();
        this.country = source.getCountry();
        this.weatherId = source.getWeatherId();
        this.weatherMainDescription = source.getWeatherMainDescription();
        this.weatherDescription = source.getWeatherDescription();
        this.weatherIcon = source.getWeatherIcon();
        this.temperature = source.getTemperature();
        this.tempFeelsLike = source.getTempFeelsLike();
        this.minTemp = source.getMinTemp();
        this.maxTemp = source.getMaxTemp();
        this.humidity = source.getHumidity();
        this.windSpeed = source.getWindSpeed();
        this.formattedDateTime = source.getFormattedDateTime();
        this.sunrise = source.getSunrise();
        this.sunset = source.getSunset();
    }

    public String getCityName() {
        return this.cityName;
    }

    public String getCountry() {
        return this.country;
    }

    public int getWeatherId() {
        return this.weatherId;
    }

    public String getWeatherMainDescription() {
        return this.weatherMainDescription;
    }

    public String getWeatherDescription() {
        return this.weatherDescription;
    }

    public String getWeatherIcon() {
        return this.weatherIcon;
    }

    public int getTemperature() {
        return this.temperature;
    }

    public int getTempFeelsLike() {
        return this.tempFeelsLike;
    }

    public int getMinTemp() {
        return this.minTemp;
    }

    public int getMaxTemp() {
        return this.maxTemp;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public int getWindSpeed() {
        return this.windSpeed;
    }

    public String getFormattedDateTime() {
        return this.formattedDateTime;
    }

    public String getSunrise() {
        return this.sunrise;
    }

    public String getSunset() {
        return this.sunset;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WeatherDataDTO)) {
            return false;
        }
        WeatherDataDTO weatherDataDTO = (WeatherDataDTO) o;
        return Objects.equals(cityName, weatherDataDTO.cityName) && Objects.equals(country, weatherDataDTO.country) && weatherId == weatherDataDTO.weatherId && Objects.equals(weatherMainDescription, weatherDataDTO.weatherMainDescription) && Objects.equals(weatherDescription, weatherDataDTO.weatherDescription) && Objects.equals(weatherIcon, weatherDataDTO.weatherIcon) && temperature == weatherDataDTO.temperature && tempFeelsLike == weatherDataDTO.tempFeelsLike && minTemp == weatherDataDTO.minTemp && maxTemp == weatherDataDTO.maxTemp && humidity == weatherDataDTO.humidity && windSpeed == weatherDataDTO.windSpeed && Objects.equals(sunrise, weatherDataDTO.sunrise) && Objects.equals(sunset, weatherDataDTO.sunset) && Objects.equals(formattedDateTime, weatherDataDTO.formattedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, country, weatherId, weatherMainDescription, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed, sunrise, sunset, formattedDateTime);
    }

    @Override
    public String toString() {
        String str = String.format("WeatherData{city:'%s', country:'%s', weatherId:'%s', weatherMainDescription:'%s', weatherDescription:'%s', weatherIcon:'%s', temperature:'%s', tempFeelsLike:'%s', minTemp:'%s', maxTemp:'%s', humidity:'%s', windSpeed:'%s', formattedDateTime:'%s', sunrise:'%s', sunet:'%s'}",
                cityName, country, weatherId, weatherMainDescription, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed, formattedDateTime, sunrise, sunset);
        return str;
    }

}
