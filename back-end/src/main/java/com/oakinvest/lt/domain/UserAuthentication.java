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
     * Equals.
     *
     * @param otherValue other value
     * @return true if equals
     */
    public boolean equals(final String otherValue) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return value.equals(otherValue);
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
