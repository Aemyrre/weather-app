package toyprojects.weatherapp.exception;

public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException() {
        super("Request limit exceeded. Try again after a minute or two.");
    }
}
