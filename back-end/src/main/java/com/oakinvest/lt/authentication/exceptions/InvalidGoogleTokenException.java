package com.oakinvest.lt.authentication.exceptions;

/**
 * Invalid token exception.
 */
public class InvalidGoogleTokenException extends InvalidTokenException {

    /**
     * Invalid token exception.
     * @param message message
     */
    public InvalidGoogleTokenException(final String message) {
        super(message);
    }

}
