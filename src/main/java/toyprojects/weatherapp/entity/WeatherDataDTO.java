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


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WeatherDataDTO)) {
            return false;
        }
        WeatherDataDTO weatherDataDTO = (WeatherDataDTO) o;
        return Objects.equals(cityName, weatherDataDTO.cityName) && Objects.equals(country, weatherDataDTO.country) && weatherId == weatherDataDTO.weatherId && Objects.equals(weatherDescription, weatherDataDTO.weatherDescription) && Objects.equals(weatherIcon, weatherDataDTO.weatherIcon) && Objects.equals(temperature, weatherDataDTO.temperature) && Objects.equals(tempFeelsLike, weatherDataDTO.tempFeelsLike) && Objects.equals(minTemp, weatherDataDTO.minTemp) && Objects.equals(maxTemp, weatherDataDTO.maxTemp) && humidity == weatherDataDTO.humidity && Objects.equals(windSpeed, weatherDataDTO.windSpeed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName, country, weatherId, weatherDescription, weatherIcon, temperature, tempFeelsLike, minTemp, maxTemp, humidity, windSpeed);
    }
    

}
