package toyprojects.weatherapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import toyprojects.weatherapp.controller.WeatherController;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @ExceptionHandler(CityNotFoundException.class)
    public ModelAndView handleCityNotFoundException(CityNotFoundException ex, HttpServletResponse response) {
        logger.error("CityNotFoundException Occured: {}", ex.getMessage());
        return createErrorModelAndView(response, HttpStatus.NOT_FOUND, "city-not-found",
                "Oops! Something went wrong!", ex.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ModelAndView handleInvalidArgumentException(InvalidInputException ex, HttpServletResponse response) {
        logger.error("InvalidInputException Occured: {}", ex.getMessage());
        return createErrorModelAndView(response, HttpStatus.BAD_REQUEST, "invalid-input",
                "Whoopsie-daisy! It seems your input has gone on vacation.", "Let’s reel it back in and try again, shall we?");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParameterException(MissingServletRequestParameterException ex, HttpServletResponse response) {
        logger.error("MissingServletRequestParameterException Occured: {}", ex.getMessage());
        return createErrorModelAndView(response, HttpStatus.BAD_REQUEST, "invalid-input",
                "Oh no, your request got caught in a hurricane of errors!", "Let’s clear the skies and give it another shot.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex, HttpServletResponse response) {
        logger.error("IllegalArgumentException Occured: {}", ex.getMessage());
        return createErrorModelAndView(response, HttpStatus.BAD_REQUEST, "unexpected-error",
                "This input feels like a tornado in a no-fly zone.", "Let’s clear the air and fix it!");
    }

    @ExceptionHandler(InvalidApiKeyException.class)
    public ModelAndView handleInvalidApiKeyException(InvalidApiKeyException ex, HttpServletResponse response) {
        logger.error("InvalidApiKeyException Occured: {}", ex.getMessage());
        return createErrorModelAndView(response, HttpStatus.UNAUTHORIZED, "unauthorized",
                "Oops! You’ve stumbled into a restricted area.", "Let’s get you back on track—authorized personnel only beyond this point!");
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ModelAndView handleRateLimitExceededException(RateLimitExceededException ex, HttpServletResponse response) {
        logger.error("RateLimitExceededException Occured: {}", ex.getMessage());
        return createErrorModelAndView(response, HttpStatus.TOO_MANY_REQUESTS, "limit",
                "Slow Down, Please!", ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex, HttpServletResponse response) {
        logger.error("RuntimeException Occured: {}", ex.getMessage());
        return createErrorModelAndView(response, HttpStatus.INTERNAL_SERVER_ERROR, "unexpected-error",
                "It looks like we’ve hit a storm in the code!", "Runtime exception detected—let’s clear the skies and try again.");
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleUnknownException(Exception ex, HttpServletResponse response) {
        logger.error("Unknown error occurred: {}", ex.getMessage());
        return createErrorModelAndView(response, HttpStatus.INTERNAL_SERVER_ERROR, "unknown-error",
                "Uh-oh! Something unexpected happened.",
                "Ramyr is working hard to resolve the issue. Please try again later.");
    }

    /**
     * Helper method to simplify the creation of Error ModelAndView object
     *
     * @param response
     * @param status
     * @param viewName
     * @param errorTitle
     * @param errorMessage
     * @return
     */
    private ModelAndView createErrorModelAndView(HttpServletResponse response,
            HttpStatus status, String errorImageClass,
            String errorTitle, String errorMessage) {

        logger.error("Exception caught in handler: error title={}, error message={}", errorTitle, errorMessage);
        response.setStatus(status.value());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorImageClass", errorImageClass);
        modelAndView.addObject("errorTitle", errorTitle);
        modelAndView.addObject("errorMessage", errorMessage);

        return modelAndView;
    }
}
