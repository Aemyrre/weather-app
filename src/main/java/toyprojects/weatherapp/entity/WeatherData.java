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

    private String sunrise;
    private String sunset;

    private String formattedDateTime;

    public WeatherData() {
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public WeatherData(String cityName, String country, int weatherId, String weatherMainDescription, String weatherDescription, String weatherIcon, double temperature, double tempFeelsLike, double minTemp, double maxTemp, int humidity, double windSpeed, Long unix, Integer timezone, Long sunrise, Long sunset) {
        setCityName(cityName);
        setCountry(country);
        setWeatherId(weatherId);
        setWeatherMainDescription(weatherMainDescription);
        setWeatherDescription(weatherDescription);
        setWeatherIcon(weatherIcon);
        setTemperature(temperature);
        setTempFeelsLike(tempFeelsLike);
        setMaxTemp(maxTemp);
        setMinTemp(minTemp);
        setHumidity(humidity);
        setWindSpeed(windSpeed);
        setFormattedDateTime(unix, timezone);
        setSunrise(sunrise, timezone);
        setSunset(sunset, timezone);
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
        return this.weatherMainDescription;
    }

    public void setWeatherMainDescription(String weatherMainDescription) {
        this.weatherMainDescription = weatherMainDescription.toLowerCase();
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

    public void setTemperature(double temperature) {
        this.temperature = (int) Math.round(temperature);
    }

    public int getTempFeelsLike() {
        return this.tempFeelsLike;
    }

    public void setTempFeelsLike(double tempFeelsLike) {
        this.tempFeelsLike = (int) Math.round(tempFeelsLike);
    }

    public int getMinTemp() {
        return this.minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = (int) Math.round(minTemp);
    }

    public int getMaxTemp() {
        return this.maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = (int) Math.round(maxTemp);
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

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = (int) Math.round(windSpeed);
    }

    public String getFormattedDateTime() {
        return this.formattedDateTime;
    }

    public void setFormattedDateTime(Long unix, Integer timezone) {
        if (unix == null) {
            this.formattedDateTime = null;
        } else {
            this.formattedDateTime = convertUnixToDateTime(unix, timezone);
        }
    }

    public String getSunrise() {
        return this.sunrise;
    }

    public void setSunrise(Long sunrise, Integer timezone) {
        if (sunrise == null) {
            this.sunrise = null;
        } else {
            this.sunrise = convertUnixToDateTime(sunrise, timezone).substring(13);
        }
    }

    public String getSunset() {
        return this.sunset;
    }

    public void setSunset(Long sunset, Integer timezone) {
        if (sunset == null) {
            this.sunset = null;
        } else {
            this.sunset = convertUnixToDateTime(sunset, timezone).substring(13);
        }
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
        return Objects.equals(cityName, weatherData.cityName) && Objects.equals(country, weatherData.country) && weatherId == weatherData.weatherId && Objects.equals(weatherMainDescription, weatherData.weatherMainDescription) && Objects.equals(weatherDescription, weatherData.weatherDescription) && Objects.equals(weatherIcon, weatherData.weatherIcon) && temperature == weatherData.temperature && tempFeelsLike == weatherData.tempFeelsLike && minTemp == weatherData.minTemp && maxTemp == weatherData.maxTemp && humidity == weatherData.humidity && windSpeed == weatherData.windSpeed && Objects.equals(sunrise, weatherData.sunrise) && Objects.equals(sunset, weatherData.sunset) && Objects.equals(formattedDateTime, weatherData.formattedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, country, weatherId, weatherMainDescription, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed, sunrise, sunset, formattedDateTime);
    }

    @Override
    public String toString() {
        String str = String.format("WeatherData{city:'%s', country:'%s', weatherId:'%s', weatherMainDescription:'%s', weatherDescription:'%s', weatherIcon:'%s', temperature:'%s', tempFeelsLike:'%s', minTemp:'%s', maxTemp:'%s', humidity:'%s', windSpeed:'%s', formattedDateTime:'%s', sunrise:'%s', sunset:'%s'}",
                cityName, country, weatherId, weatherMainDescription, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed, formattedDateTime, sunrise, sunset);
        return str;
    }

}
