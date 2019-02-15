package com.oakinvest.lt.authentication.exceptions;

/**
 * Invalid token exception.
 */
class InvalidTokenException extends Exception {

    /**
     * Invalid token exception.
     * @param message error message
     */
    InvalidTokenException(final String message) {
        super(message);
    }

}
