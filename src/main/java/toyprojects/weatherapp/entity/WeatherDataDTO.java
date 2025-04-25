package toyprojects.weatherapp.entity;

import java.util.Objects;

public class WeatherDataDTO {

    private final String cityName;
    private final String country;

    private final int weatherId;
    private final String weatherMainDescription;
    private final String weatherDescription;
    private final String weatherIcon;

    private final int temperature;
    private final int tempFeelsLike;
    private final int minTemp;
    private final int maxTemp;
    private final int humidity;

    private final int windSpeed;

    private final String sunrise;
    private final String sunset;

    private final String formattedDateTime;

    private static final String UNKNOWN = "unknown";

    public WeatherDataDTO(WeatherData source) {
        this.cityName = source.getCityName() != null
                ? source.getCityName()
                : UNKNOWN;

        this.country = source.getCountry() != null
                ? source.getCountry()
                : UNKNOWN;

        this.weatherId = source.getWeatherId();

        this.weatherMainDescription = source.getWeatherMainDescription() != null
                ? source.getWeatherMainDescription()
                : UNKNOWN;

        this.weatherDescription = source.getWeatherDescription() != null
                ? source.getWeatherDescription()
                : UNKNOWN;

        this.weatherIcon = source.getWeatherIcon() != null
                ? source.getWeatherIcon()
                : UNKNOWN;

        this.temperature = source.getTemperature();

        this.tempFeelsLike = source.getTempFeelsLike();

        this.minTemp = source.getMinTemp();

        this.maxTemp = source.getMaxTemp();

        this.humidity = source.getHumidity();

        this.windSpeed = source.getWindSpeed();

        this.formattedDateTime = source.getFormattedDateTime() != null
                ? source.getFormattedDateTime()
                : UNKNOWN;

        this.sunrise = source.getSunrise() != null
                ? source.getSunrise()
                : UNKNOWN;

        this.sunset = source.getSunset() != null
                ? source.getSunset()
                : UNKNOWN;
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
        return Objects.equals(cityName, weatherDataDTO.cityName) && Objects.equals(country, weatherDataDTO.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, country);
    }

    @Override
    public String toString() {
        String str = String.format("WeatherData{city:'%s', country:'%s', weatherId:'%s', weatherMainDescription:'%s', weatherDescription:'%s', weatherIcon:'%s', temperature:'%s', tempFeelsLike:'%s', minTemp:'%s', maxTemp:'%s', humidity:'%s', windSpeed:'%s', formattedDateTime:'%s', sunrise:'%s', sunet:'%s'}",
                cityName, country, weatherId, weatherMainDescription, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed, formattedDateTime, sunrise, sunset);
        return str;
    }

}
