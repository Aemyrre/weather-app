package toyprojects.weatherapp.exception;

public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String message) {
        super(message);
    }

    public CityNotFoundException() {
        super("We can't find the city!");
    }
}
