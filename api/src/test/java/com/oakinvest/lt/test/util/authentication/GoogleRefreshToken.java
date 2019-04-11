package com.oakinvest.lt.test.util.authentication;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Google Token Response.
 */
@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleRefreshToken implements Serializable {

    /**
     * A token that can be sent to a Google API.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * The remaining lifetime of the access token. 3600 seconds.
     */
    @JsonProperty("expires_in")
    private long expiresIn;

    /**
     * scope.
     */
    @JsonProperty("scope")
    private String scope;

    /**
     * Identifies the type of token returned. At this time, this field always has the value Bearer.
     */
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * A JWT that contains identity information about the account that is digitally signed by Google.
     */
    @JsonProperty("id_token")
    private String idToken;

    /**
     * Get access token.
     * @return access token
     */
    public final String getAccessToken() {
        return accessToken;
    }

    /**
     * Set access token.
     * @param newAccessToken access token
     */
    public final void setAccessToken(final String newAccessToken) {
        this.accessToken = newAccessToken;
    }

    /**
     * Get expires in.
     * @return expires in
     */
    public final long getExpiresIn() {
        return expiresIn;
    }

    /**
     * Set expires in.
     * @param newExpiresIn expires in
     */
    public final void setExpiresIn(final long newExpiresIn) {
        this.expiresIn = newExpiresIn;
    }

    /**
     * Get scope.
     * @return scope
     */
    public final String getScope() {
        return scope;
    }

    /**
     * Set scope.
     * @param newScope scope
     */
    public final void setScope(final String newScope) {
        this.scope = newScope;
    }

    /**
     * Get token type.
     * @return token type
     */
    public final String getTokenType() {
        return tokenType;
    }

    /**
     * Set token type.
     * @param newTokenType token type
     */
    public final void setTokenType(final String newTokenType) {
        this.tokenType = newTokenType;
    }

    /**
     * Get id token.
     * @return if token
     */
    public final String getIdToken() {
        return idToken;
    }

    /**
     * Set id token.
     * @param newIddToken id token
     */
    public final void setIdToken(final String newIddToken) {
        this.idToken = newIddToken;
    }

}
