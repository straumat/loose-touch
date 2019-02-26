package com.oakinvest.lt.test.util.authentication;

/**
 * Google test users.
 */
public enum GoogleTestUsers {

    /**
     * loose.touch.test.1@gmail.com.
     */
    USER_1("loose 1",
            "touch 1",
            "loose.touch.test.1@gmail.com",
            "https://lh5.googleusercontent.com/-vTIMhyL9ePM/AAAAAAAAAAI/AAAAAAAAAAA/ACevoQPEHAQw-lr-v1PCh4yr9AsWWmrITQ/s96-c/photo.jpg"),

    /**
     * loose.touch.test.2@gmail.com.
     */
    USER_2("loose 2",
            "touch 2",
            "loose.touch.test.2@gmail.com",
            "https://lh3.googleusercontent.com/-GxmjZPF4TI8/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rc7nhCokY3mTuBd80Fk1nLAvs96wQ/mo/photo.jpg");

    /**
     * First name.
     */
    private String firstName;

    /**
     * Last name.
     */
    private String lastName;

    /**
     * Email.
     */
    private String email;

    /**
     * Profile image url.
     */
    private String pictureUrl;

    /**
     * Constructor.
     *
     * @param newFirstName first name
     * @param newLastName  last name
     * @param newEmail     email
     * @param newImageUrl  image url
     */
    GoogleTestUsers(final String newFirstName, final String newLastName, final String newEmail, final String newImageUrl) {
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.email = newEmail;
        this.pictureUrl = newImageUrl;
    }

    /**
     * Get firstName.
     *
     * @return firstName
     */
    public final String getFirstName() {
        return firstName;
    }

    /**
     * Get lastName.
     *
     * @return lastName
     */
    public final String getLastName() {
        return lastName;
    }

    /**
     * Get email.
     *
     * @return email
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Get pictureUrl.
     *
     * @return pictureUrl
     */
    public final String getPictureUrl() {
        return pictureUrl;
    }

}
