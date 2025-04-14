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
        return createErrorModelAndView(response, HttpStatus.NOT_FOUND,
                "Oops! Something went wrong!", ex.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ModelAndView handleInvalidArgumentException(InvalidInputException ex, HttpServletResponse response) {
        return createErrorModelAndView(response, HttpStatus.BAD_REQUEST,
                "Invalid Input", ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleInvalidArgumentException(MissingServletRequestParameterException ex, HttpServletResponse response) {
        return createErrorModelAndView(response, HttpStatus.BAD_REQUEST,
                "Invalid Input", ex.getMessage());
    }

    @ExceptionHandler(InvalidApiKeyException.class)
    public ModelAndView handleInvalidApiKeyException(InvalidApiKeyException ex, HttpServletResponse response) {
        return createErrorModelAndView(response, HttpStatus.UNAUTHORIZED,
                "Unauthorized", ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex, HttpServletResponse response) {
        return createErrorModelAndView(response, HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected Error", ex.getMessage());
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
            HttpStatus status,
            String errorTitle, String errorMessage) {

        logger.error("Exception caught in handler: {}", errorMessage);
        response.setStatus(status.value());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorTitle", errorTitle);
        modelAndView.addObject("errorMessage", errorMessage);

        return modelAndView;
    }
}
