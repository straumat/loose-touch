package com.oakinvest.lt.authentication.loosetouch;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

/**
 * Loose touch Jwt token provider.
 * This class creates token for the application and also have a method to validate the token coming from the client.
 *
 * We are using JWT to createContact the token for our application.
 *
 * JWT is RFC 7519 standard for representing and sharing claims securely between communicating parties.
 */
@Component
public class LooseTouchTokenProvider {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(LooseTouchTokenProvider.class);

    /**
     * Secret key.
     */
    @Value("${application.jwt.secretKey}")
    private String secretKey;

    /**
     * Token expiration delay - 30 days.
     */
    @Value("${application.jwt.expirationDelay}")
    private long defaultExpirationDelay;

    /**
     * Creates a token for the given accountId (with application default delay.
     * @param accountId accountId
     * @return token
     */
    public final String createToken(final String accountId) {
        return createToken(accountId, defaultExpirationDelay);
    }

    /**
     * Creates a token for the given accountId with an expiration delay.
     * @param accountId accountId
     * @param expirationDelay expiration delay
     * @return token
     */
    public final String createToken(final String accountId, final long expirationDelay) {
        log.debug("Creating a token for account " + accountId);

        // Set accountId.
        Claims claims = Jwts.claims().setSubject(accountId);

        // Set issued date & validity.
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationDelay);

        // Creates the signing key.
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // Creates and returns the token.
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(signingKey)
                .compact();
    }

    /**
     * Returns the userId associated with the token.
     * @param token token
     * @return userId
     */
    public final Optional<String> getAccountId(final String token) {
        log.debug("Getting account id for token " + token);
        try {
            // Getting data from the claims.
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            // Checking that the token is not expired.
            if (claims.getBody().getExpiration().before(new Date())) {
                log.debug("Token " + token + " expired");
                return Optional.empty();
            } else {
                // Return the accountId.
                String accountId = claims.getBody().getSubject();
                log.debug("Token " + token + " is from account " + accountId);
                return Optional.of(accountId);
            }
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid token : " + token);
            return Optional.empty();
        }
    }

}
