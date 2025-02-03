package toyprojects.weatherapp.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class WeatherData {

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

    public WeatherData() {
    }

    public WeatherData(String cityName, String country, int weatherId, String weatherDescription, String weatherIcon, BigDecimal temperature, BigDecimal tempFeelsLike, BigDecimal minTemp, BigDecimal maxTemp, int humidity, BigDecimal windSpeed, Long unix, Integer timezone) {
        this.cityName = cityName;
        this.country = country;
        this.weatherId = weatherId;
        this.weatherDescription = weatherDescription;
        this.weatherIcon = weatherIcon;
        this.temperature = temperature;
        this.tempFeelsLike = tempFeelsLike;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.formattedDateTime = convertUnixToDateTime(unix, timezone);
    }

    private String convertUnixToDateTime(Long unixTime, Integer timeZone) {
        Instant instant = Instant.ofEpochSecond(unixTime);
        ZonedDateTime dateTime = instant.atZone(ZoneOffset.ofTotalSeconds(timeZone));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return String.format("%s", dateTime.format(formatter));
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getWeatherId() {
        return this.weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherDescription() {
        return this.weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherIcon() {
        return this.weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public BigDecimal getTemperature() {
        return this.temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getTempFeelsLike() {
        return this.tempFeelsLike;
    }

    public void setTempFeelsLike(BigDecimal tempFeelsLike) {
        this.tempFeelsLike = tempFeelsLike;
    }

    public BigDecimal getMinTemp() {
        return this.minTemp;
    }

    public void setMinTemp(BigDecimal minTemp) {
        this.minTemp = minTemp;
    }

    public BigDecimal getMaxTemp() {
        return this.maxTemp;
    }

    public void setMaxTemp(BigDecimal maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public BigDecimal getWindSpeed() {
        return this.windSpeed;
    }

    public void setWindSpeed(BigDecimal windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getFormattedDateTime() {
        return this.formattedDateTime;
    }

    public void setFormattedDateTime(Long unix, Integer timezone) {
        this.formattedDateTime = convertUnixToDateTime(unix, timezone);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WeatherData)) {
            return false;
        }
        WeatherData weatherData = (WeatherData) o;
        return Objects.equals(cityName, weatherData.cityName) && Objects.equals(country, weatherData.country) && weatherId == weatherData.weatherId && Objects.equals(weatherDescription, weatherData.weatherDescription) && Objects.equals(weatherIcon, weatherData.weatherIcon) && Objects.equals(temperature, weatherData.temperature) && Objects.equals(tempFeelsLike, weatherData.tempFeelsLike) && Objects.equals(minTemp, weatherData.minTemp) && Objects.equals(maxTemp, weatherData.maxTemp) && humidity == weatherData.humidity && Objects.equals(windSpeed, weatherData.windSpeed) && Objects.equals(formattedDateTime, weatherData.formattedDateTime);
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
