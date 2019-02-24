package com.oakinvest.lt.dto.v1;

/**
 * User DTO.
 */
public class UserDTO {

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
    private String imageUrl;

    /**
     * Get firstName.
     *
     * @return firstName
     */
    public final String getFirstName() {
        return firstName;
    }

    /**
     * Set firstName.
     *
     * @param newFirstName the firstName to set
     */
    public final void setFirstName(final String newFirstName) {
        firstName = newFirstName;
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
     * Set lastName.
     *
     * @param newLastName the lastName to set
     */
    public final void setLastName(final String newLastName) {
        lastName = newLastName;
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
     * Set email.
     *
     * @param newEmail the email to set
     */
    public final void setEmail(final String newEmail) {
        email = newEmail;
    }

    /**
     * Get imageUrl.
     *
     * @return imageUrl
     */
    public final String getImageUrl() {
        return imageUrl;
    }

    /**
     * Set imageUrl.
     *
     * @param newImageUrl the imageUrl to set
     */
    public final void setImageUrl(final String newImageUrl) {
        imageUrl = newImageUrl;
    }
}
