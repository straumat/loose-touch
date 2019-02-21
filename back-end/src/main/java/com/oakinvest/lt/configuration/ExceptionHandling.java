package com.oakinvest.lt.configuration;

import com.oakinvest.lt.util.rest.LooseTouchError;
import com.oakinvest.lt.util.rest.LooseTouchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exception handling controller.
 */
@ControllerAdvice
public class ExceptionHandling {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(ExceptionHandling.class);

    /**
     * Deal with cerise exception.
     *
     * @param ex exception
     * @return rest error
     */
    @ExceptionHandler(LooseTouchException.class)
    public final ResponseEntity<LooseTouchError> looseTouchException(final LooseTouchException ex) {
        // Logging.
        log.error("Error : " + ex);

        // Creating response.
        LooseTouchError response = new LooseTouchError(ex.getType(), ex.getMessage(), ex.getErrors());

        // Choosing http status.
        HttpStatus status;
        switch (ex.getType()) {
            case api_connection_error:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            case api_error:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            case authentication_error:
                status = HttpStatus.UNAUTHORIZED;
                break;
            case invalid_request_error:
                status = HttpStatus.BAD_REQUEST;
                break;
            case rate_limit_error:
                status = HttpStatus.TOO_MANY_REQUESTS;
                break;
            case unspecified_error:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            case unknown_error:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        // returning error.
        return new ResponseEntity<>(response, status);
    }

}
