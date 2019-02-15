package com.oakinvest.lt.authentication.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

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
     * @param idToken token
     * @return payload.
     */
    public final Optional<Payload> verifyToken(final String idToken) {
        log.debug("Verifying token " + idToken);

        // Creates the google token verifier.
        final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.
                Builder(TRANSPORT, JSON_FACTORY)
                .setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
                .setAudience(Collections.singletonList(clientId))
                .build();

        // Token verification.
        GoogleIdToken googleIdToken = null;
        try {
            googleIdToken = verifier.verify(idToken);
        } catch (IllegalArgumentException | GeneralSecurityException | IOException e) {
            log.debug("Error during token verification : " + e.getMessage());
            return Optional.empty();
        }

        // If token is null, the token is invalid.
        if (googleIdToken == null) {
            log.debug("Error during token verification googleIdToken is null");
            return Optional.empty();
        } else {
            // We return the parsed token information.
            log.debug("Token " + idToken + " is valid");
            return Optional.of(googleIdToken.getPayload());
        }
    }

}
