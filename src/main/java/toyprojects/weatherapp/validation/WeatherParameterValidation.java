package toyprojects.weatherapp.validation;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import toyprojects.weatherapp.constants.ValidLanguage;
import toyprojects.weatherapp.constants.ValidUnits;
import toyprojects.weatherapp.exception.CityNotFoundException;

@Component
public class WeatherParameterValidation {

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
            units = "me";
        }

        String tempUnit = units;
        boolean isValidUnit = Arrays.stream(ValidUnits.values())
                .anyMatch(unit -> unit.name().equalsIgnoreCase(tempUnit));

        return !isValidUnit ? "metric" : units;
    }

    public String validateLanguage(String lang) {
        logger.info("Validating instance variable: language={}", lang);
        if (lang == null || lang.trim().isEmpty() || lang.isBlank()) {
            lang = "en";
        }

        String tempLang = lang;
        boolean isValidUnit = Arrays.stream(ValidLanguage.values())
                .anyMatch(unit -> unit.name().equalsIgnoreCase(tempLang));

        return !isValidUnit ? "en" : lang;
    }

    public void validateCoordinates(double lat, double lon) {
        logger.info("Validating instance variable: cooredinates: lat={}, lon={}", lat, lon);
        if (lat < -90 || lat > 90
                || lon < -180 || lon > 180) {
            logger.warn("City not found. Invalid coordinates: lat={}, lon={}", lat, lon);
            throw new CityNotFoundException();
        }
    }
}
