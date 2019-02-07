package com.oakinvest.lt.util.auth.exceptions;

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
