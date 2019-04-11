package com.oakinvest.lt.authentication.loosetouch;

/**
 * Authenticated account.
 */
@SuppressWarnings("unused")
public class AuthenticatedAccount {

    /**
     * Loose touch token.
     */
    private String looseTouchToken;

    /**
     * Account id.
     */
    private String accountId;

    /**
     * Constructor.
     *
     * @param newLooseTouchToken loose touch token.
     * @param newAccountId          account id.
     */
    AuthenticatedAccount(final String newLooseTouchToken, final String newAccountId) {
        this.looseTouchToken = newLooseTouchToken;
        this.accountId = newAccountId;
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
     * Get accountId.
     *
     * @return accountId
     */
    public final String getAccountId() {
        return accountId;
    }

    /**
     * Set accountId.
     *
     * @param newAccountId the accountId to set
     */
    public final void setAccountId(final String newAccountId) {
        accountId = newAccountId;
    }

}
