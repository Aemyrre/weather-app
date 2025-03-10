package toyprojects.weatherapp.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class WeatherDataDTO {

    private String cityName;
    private String country;

    private int weatherId;
    private String weatherDescription;
    private String weatherIcon;

    private BigDecimal temperature;
    private BigDecimal tempFeelsLike;
    private BigDecimal minTemp;
    private BigDecimal maxTemp;
    private int humidity;

    private BigDecimal windSpeed;

    private String formattedDateTime;

    public WeatherDataDTO() {
    }

    public WeatherDataDTO(WeatherData source) {
        this.cityName = source.getCityName();
        this.country = source.getCountry();
        this.weatherId = source.getWeatherId();
        this.weatherDescription = source.getWeatherDescription();
        this.weatherIcon = source.getWeatherIcon();
        this.temperature = source.getTemperature();
        this.tempFeelsLike = source.getTempFeelsLike();
        this.minTemp = source.getMinTemp();
        this.maxTemp = source.getMaxTemp();
        this.humidity = source.getHumidity();
        this.windSpeed = source.getWindSpeed();
        this.formattedDateTime = source.getFormattedDateTime();
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

    public String getWeatherDescription() {
        return this.weatherDescription;
    }

    public String getWeatherIcon() {
        return this.weatherIcon;
    }

    public BigDecimal getTemperature() {
        return this.temperature;
    }

    public BigDecimal getTempFeelsLike() {
        return this.tempFeelsLike;
    }

    public BigDecimal getMinTemp() {
        return this.minTemp;
    }

    public BigDecimal getMaxTemp() {
        return this.maxTemp;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public BigDecimal getWindSpeed() {
        return this.windSpeed;
    }

    public String getFormattedDateTime() {
        return this.formattedDateTime;
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
        return Objects.equals(cityName, weatherDataDTO.cityName) && Objects.equals(country, weatherDataDTO.country) && weatherId == weatherDataDTO.weatherId && Objects.equals(weatherDescription, weatherDataDTO.weatherDescription) && Objects.equals(weatherIcon, weatherDataDTO.weatherIcon) && Objects.equals(temperature, weatherDataDTO.temperature) && Objects.equals(tempFeelsLike, weatherDataDTO.tempFeelsLike) && Objects.equals(minTemp, weatherDataDTO.minTemp) && Objects.equals(maxTemp, weatherDataDTO.maxTemp) && humidity == weatherDataDTO.humidity && Objects.equals(windSpeed, weatherDataDTO.windSpeed) && Objects.equals(formattedDateTime, weatherDataDTO.formattedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, country, weatherId, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed, formattedDateTime);
    }

    @Override
    public String toString() {
        String str = String.format("WeatherData{city:'%s', country:'%s', weatherId:'%s', weatherDescription:'%s', weatherIcon:'%s', temperature:'%s', tempFeelsLike:'%s', minTemp:'%s', maxTemp:'%s', humidity:'%s', windSpeed:'%s', formattedDateTime:'%s'}",
                cityName, country, weatherId, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed, formattedDateTime);
        return str;
    }

}
