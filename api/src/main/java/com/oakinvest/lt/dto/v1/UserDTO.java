package com.oakinvest.lt.dto.v1;

import io.swagger.annotations.ApiModelProperty;

/**
 * User DTO.
 */
@SuppressWarnings({"magicnumber", "unused"})
public class UserDTO {

    /**
     * ID Token.
     */
    @ApiModelProperty(value = "Loose touch valid id token",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2ZTRlY2MwYi0xNjhiLTQ3OWQtYTQzNC0xMzA0NGZhNzcyZWIiLCJpYXQiOjE1NTExNzk1OTcsImV4cCI6MTU1Mzc3MTU5N30.ckBAMqWO2BkZiv4xCNIGR_GLiQkPfMQW5NLxZsQSqGs",
            required = true,
            position = 1)
    private String idToken;

    /**
     * First name.
     */
    @ApiModelProperty(value = "First name",
            example = "loose 1",
            required = true,
            position = 2)
    private String firstName;

    /**
     * Last name.
     */
    @ApiModelProperty(value = "Last name",
            example = "touch 1",
            required = true,
            position = 3)
    private String lastName;

    /**
     * Email.
     */
    @ApiModelProperty(value = "Email",
            example = "loose.touch.test.1@gmail.com",
            required = true,
            position = 4)
    private String email;

    /**
     * Profile image url.
     */
    @ApiModelProperty(value = "Picture url",
            example = "https://lh5.googleusercontent.com/-vTIMhyL9ePM/AAAAAAAAAAI/AAAAAAAAAAA/ACevoQPEHAQw-lr-v1PCh4yr9AsWWmrITQ/s96-c/photo.jpg",
            required = true,
            position = 5)
    private String pictureUrl;

    /**
     * Indicates if this account has just been created.
     */
    @ApiModelProperty(value = "Indicates if this account has just been created",
            example = "true",
            required = true,
            position = 6)
    private boolean newAccount = false;

    /**
     * Get idToken.
     *
     * @return idToken
     */
    public final String getIdToken() {
        return idToken;
    }

    /**
     * Set idToken.
     *
     * @param newIdToken the idToken to set
     */
    public final void setIdToken(final String newIdToken) {
        idToken = newIdToken;
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
     * Get pictureUrl.
     *
     * @return pictureUrl
     */
    public final String getPictureUrl() {
        return pictureUrl;
    }

    /**
     * Set pictureUrl.
     *
     * @param newImageUrl the pictureUrl to set
     */
    public final void setPictureUrl(final String newImageUrl) {
        pictureUrl = newImageUrl;
    }

    /**
     * Get newAccount.
     *
     * @return newAccount
     */
    public final boolean isNewAccount() {
        return newAccount;
    }

    /**
     * Set newAccount.
     *
     * @param newNewAccount the newAccount to set
     */
    public final void setNewAccount(final boolean newNewAccount) {
        newAccount = newNewAccount;
    }

}
