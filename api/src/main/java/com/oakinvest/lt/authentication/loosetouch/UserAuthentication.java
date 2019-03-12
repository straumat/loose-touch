package com.oakinvest.lt.authentication.loosetouch;

/**
 * User authentication type.
 */
@SuppressWarnings("unused")
public enum UserAuthentication {

    /**
     * Authentication with google.
     */
    GOOGLE("google"),

    /**
     * Twitter.
     */
    TWITTER("twitter");

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
