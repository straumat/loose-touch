package com.oakinvest.lt.dto.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Google user info dto.
 */
@JsonInclude(NON_NULL)
public class GoogleUserInfoDTO {

    /**
     * Email.
     */
    @JsonProperty("email")
    private String email;

    /**
     * Given name.
     */
    @JsonProperty("given_name")
    private String givenName;

    /**
     * Family name.
     */
    @JsonProperty("family_name")
    private String familyName;

    /**
     * picture.
     */
    @JsonProperty("picture")
    private String picture;

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
     * Get givenName.
     *
     * @return givenName
     */
    public final String getGivenName() {
        return givenName;
    }

    /**
     * Set givenName.
     *
     * @param newGivenName the givenName to set
     */
    public final void setGivenName(final String newGivenName) {
        givenName = newGivenName;
    }

    /**
     * Get familyName.
     *
     * @return familyName
     */
    public final String getFamilyName() {
        return familyName;
    }

    /**
     * Set familyName.
     *
     * @param newFamilyName the familyName to set
     */
    public final void setFamilyName(final String newFamilyName) {
        familyName = newFamilyName;
    }

    /**
     * Get picture.
     *
     * @return picture
     */
    public final String getPicture() {
        return picture;
    }

    /**
     * Set picture.
     *
     * @param newPicture the picture to set
     */
    public final void setPicture(final String newPicture) {
        picture = newPicture;
    }

}
