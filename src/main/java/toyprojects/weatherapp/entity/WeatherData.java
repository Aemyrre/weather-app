package toyprojects.weatherapp.entity;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class WeatherData {

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

    private String formattedDateTime;

    public WeatherData() {
    }

    public WeatherData(String cityName, String country, int weatherId, String weatherMainDescription, String weatherDescription, String weatherIcon, double temperature, double tempFeelsLike, double minTemp, double maxTemp, int humidity, double windSpeed, Long unix, Integer timezone) {
        this.cityName = cityName;
        this.country = country;
        this.weatherId = weatherId;
        this.weatherMainDescription = weatherMainDescription.toLowerCase();
        this.weatherDescription = weatherDescription;
        this.weatherIcon = weatherIcon;
        this.temperature = (int) Math.round(temperature);
        this.tempFeelsLike = (int) Math.round(tempFeelsLike);
        this.minTemp = (int) Math.round(minTemp);
        this.maxTemp = (int) Math.round(maxTemp);
        this.humidity = humidity;
        this.windSpeed = (int) Math.round(windSpeed);
        this.formattedDateTime = convertUnixToDateTime(unix, timezone);
    }

    private String convertUnixToDateTime(Long unixTime, Integer timeZone) {
        Instant instant = Instant.ofEpochSecond(unixTime);
        ZonedDateTime dateTime = instant.atZone(ZoneOffset.ofTotalSeconds(timeZone));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

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

    public String getWeatherMainDescription() {
        return this.weatherMainDescription.toLowerCase();
    }

    public void setWeatherMainDescription(String weatherMainDescription) {
        this.weatherMainDescription = weatherMainDescription;
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

    public int getTemperature() {
        return this.temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTempFeelsLike() {
        return this.tempFeelsLike;
    }

    public void setTempFeelsLike(int tempFeelsLike) {
        this.tempFeelsLike = tempFeelsLike;
    }

    public int getMinTemp() {
        return this.minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return this.maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getHumidity() {
        return this.humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getWindSpeed() {
        return this.windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
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
        return Objects.equals(cityName, weatherData.cityName) && Objects.equals(country, weatherData.country) && weatherId == weatherData.weatherId && Objects.equals(weatherMainDescription, weatherData.weatherMainDescription) && Objects.equals(weatherDescription, weatherData.weatherDescription) && Objects.equals(weatherIcon, weatherData.weatherIcon) && Objects.equals(temperature, weatherData.temperature) && Objects.equals(tempFeelsLike, weatherData.tempFeelsLike) && Objects.equals(minTemp, weatherData.minTemp) && Objects.equals(maxTemp, weatherData.maxTemp) && humidity == weatherData.humidity && Objects.equals(windSpeed, weatherData.windSpeed) && Objects.equals(formattedDateTime, weatherData.formattedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, country, weatherId, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed, formattedDateTime);
    }

    @Override
    public String toString() {
        String str = String.format("WeatherData{city:'%s', country:'%s', weatherId:'%s', weatherMainDescription:'%s', weatherDescription:'%s', weatherIcon:'%s', temperature:'%s', tempFeelsLike:'%s', minTemp:'%s', maxTemp:'%s', humidity:'%s', windSpeed:'%s', formattedDateTime:'%s'}",
                cityName, country, weatherId, weatherMainDescription, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed, formattedDateTime);
        return str;
    }

}
