package com.oakinvest.lt.authentication.loosetouch;

/**
 * Authenticated user.
 */
public class AuthenticatedUser {

    /**
     * Loose touch token.
     */
    private String looseTouchToken;

    /**
     * User id.
     */
    private String userId;

    /**
     * Constructor.
     * @param newLooseTouchToken loose touch token.
     * @param newUuserId user id.
     */
    AuthenticatedUser(final String newLooseTouchToken, final String newUuserId) {
        this.looseTouchToken = newLooseTouchToken;
        this.userId = newUuserId;
    }

    /**
     * Get looseTouchToken.
     *
     * @return looseTouchToken
     */
    public final String getLooseTouchToken() {
        return looseTouchToken;
    }

    /**
     * Set looseTouchToken.
     *
     * @param newLooseTouchToken the looseTouchToken to set
     */
    public final void setLooseTouchToken(final String newLooseTouchToken) {
        looseTouchToken = newLooseTouchToken;
    }

    /**
     * Get userId.
     *
     * @return userId
     */
    public final String getUserId() {
        return userId;
    }

    /**
     * Set userId.
     *
     * @param newUserId the userId to set
     */
    public final void setUserId(final String newUserId) {
        userId = newUserId;
    }

}
