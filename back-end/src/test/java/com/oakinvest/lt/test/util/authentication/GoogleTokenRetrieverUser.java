package com.oakinvest.lt.test.util.authentication;

/**
 * Test users.
 */
public enum GoogleTokenRetrieverUser {

    /**
     * loose.touch.test.1@gmail.com.
     */
    USER_1("loose.touch.test.1@gmail.com"),

    /**
     * loose.touch.test.2@gmail.com.
     */
    USER_2("loose.touch.test.2@gmail.com");

    /**
     * USer email.
     */
    private final String email;

    /**
     * COnstructor.
     * @param newEmail user email.
     */
    GoogleTokenRetrieverUser(final String newEmail) {
        email = newEmail;
    }

    /**
     * Returns user email.
     * @return user email
     */
    public final String getEmail() {
        return email;
    }

}
