package toyprojects.weatherapp.exception;

public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String message) {
        super(message);
    }

    public CityNotFoundException() {
        super("City not found");
    }
}
