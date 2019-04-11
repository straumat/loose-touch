package com.oakinvest.lt.authentication.loosetouch;

/**
 * Account authentication type.
 */
@SuppressWarnings("unused")
public enum AccountAuthentication {

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
    AccountAuthentication(final String newValue) {
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
