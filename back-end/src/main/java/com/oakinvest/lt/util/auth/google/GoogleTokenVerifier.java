package com.oakinvest.lt.util.auth.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.oakinvest.lt.util.auth.exceptions.InvalidGoogleTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Google token verifier.
 * This class takes the token coming from the client and validates it with Google using Google’s Java Client library.
 */
@Component
public class GoogleTokenVerifier {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(GoogleTokenVerifier.class);

    /**
     * HTTP transport.
     */
    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    /**
     * JSON Factory.
     */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Application client ID.
     */
    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    /**
     * Verify a google token by connecting to google servers.
     * @param idTokenString token
     * @return payload.
     * @throws GeneralSecurityException general security exception.
     * @throws IOException input/output exception.
     * @throws InvalidGoogleTokenException invalid token exception.
     */
    public final Payload verifyToken(final String idTokenString)
            throws GeneralSecurityException, IOException, InvalidGoogleTokenException {
        log.debug("Verifying token " + idTokenString);

        // Creates the google token verifier.
        final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.
                Builder(TRANSPORT, JSON_FACTORY)
                .setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
                .setAudience(Collections.singletonList(clientId))
                .build();

        // Token verification.
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (IllegalArgumentException e) {
            log.debug("Error during token verification : " + e.getMessage());
            e.printStackTrace();
        }

        // If token is null, the token is invalid.
        if (idToken == null) {
            log.debug("Token " + idTokenString + " is not valid");
            throw new InvalidGoogleTokenException("Token " + idTokenString + " is invalid");
        } else {
            log.debug("Token " + idTokenString + " is valid");
        }

        // We return the parsed token information.
        return idToken.getPayload();
    }

}
