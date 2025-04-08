package toyprojects.weatherapp.validation;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import toyprojects.weatherapp.constants.ValidLanguage;
import toyprojects.weatherapp.constants.ValidUnits;
import toyprojects.weatherapp.exception.CityNotFoundException;

@Component
public class WeatherParameterValidation {

    public WeatherParameterValidation() {
    }

    public void validateCity(String city) {
        System.out.println("ValidateCity was called");
        if (city == null || city.trim().isEmpty() || city.isBlank()) {
            throw new CityNotFoundException();
        }
    }

    public String validateUnitOfMeasurement(String units) {
        System.out.println("validateUnitOfMeasurement was called");
        if (units == null || units.trim().isEmpty() || units.isBlank()) {
            units = "me";
        }

        String tempUnit = units;
        boolean isValidUnit = Arrays.stream(ValidUnits.values())
                .anyMatch(unit -> unit.name().equalsIgnoreCase(tempUnit));

        return !isValidUnit ? "metric" : units;
    }

    public String validateLanguage(String lang) {
        System.out.println("validateLanguage was called");
        if (lang == null || lang.trim().isEmpty() || lang.isBlank()) {
            lang = "en";
        }

        String tempLang = lang;
        boolean isValidUnit = Arrays.stream(ValidLanguage.values())
                .anyMatch(unit -> unit.name().equalsIgnoreCase(tempLang));

        return !isValidUnit ? "en" : lang;
    }

    public void validateCoordinates(double lat, double lon) {
        System.out.println("validateCoordinates was called");
        if (lat < -90 || lat > 90
                || lon < -180 || lon > 180) {
            throw new CityNotFoundException();
        }
    }
}
