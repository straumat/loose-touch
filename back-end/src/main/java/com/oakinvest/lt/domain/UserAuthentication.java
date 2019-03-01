package com.oakinvest.lt.domain;

/**
 * User authentication type.
 */
public enum UserAuthentication {

    /**
     * Authentication with google.
     */
    GOOGLE("google");

    /**
     * Value.
     */
    private final String value;

    /**
     * Constructor.
     *
     * @param newValue new value
     */
    UserAuthentication(final String newValue) {
        value = newValue;
    }

    /**
     * To display value.
     *
     * @return value
     */
    public String toString() {
        return this.value;
    }

}
