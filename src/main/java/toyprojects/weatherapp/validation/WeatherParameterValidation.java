package toyprojects.weatherapp.validation;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import toyprojects.weatherapp.constants.ValidLanguage;
import toyprojects.weatherapp.constants.ValidUnits;
import toyprojects.weatherapp.exception.CityNotFoundException;

@Component
public class WeatherParameterValidation {

    private static final Set<String> validUnits = Arrays.stream(ValidUnits.values())
            .map(Enum::name)
            .collect(Collectors.toSet());

    private static final Set<String> validLangugages = Arrays.stream(ValidLanguage.values())
            .map(Enum::name)
            .collect(Collectors.toSet());

    private static final Logger logger = LoggerFactory.getLogger(WeatherParameterValidation.class);

    public WeatherParameterValidation() {
    }

    public void validateCity(String city) {
        logger.info("Validating instance variable: city={}", city);
        if (city == null || city.trim().isEmpty() || city.isBlank()) {
            logger.warn("City not found. Invalid city: {}", city);
            throw new CityNotFoundException();
        }
    }

    public String validateUnitOfMeasurement(String units) {
        logger.info("Validating instance variable: unit of measurement={}", units);
        if (units == null || units.trim().isEmpty() || units.isBlank()) {
            return "metric";
        }

        return validUnits.contains(units.toUpperCase()) ? units : "metric";
    }

    public String validateLanguage(String lang) {
        logger.info("Validating instance variable: language={}", lang);
        if (lang == null || lang.trim().isEmpty() || lang.isBlank()) {
            lang = "en";
        }

        return validLangugages.contains(lang.toUpperCase()) ? lang : "en";
    }

    public void validateCoordinates(double lat, double lon) {
        logger.info("Validating instance variable: cooredinates: lat={}, lon={}", lat, lon);
        if (lat < -90 || lat > 90
                || lon < -180 || lon > 180) {
            logger.warn("Location not found. Invalid coordinates: lat={}, lon={}", lat, lon);
            throw new CityNotFoundException();
        }
    }
}
