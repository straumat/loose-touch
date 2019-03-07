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
     * Creates a token for the given userId (with application default delay.
     * @param userId userId
     * @return token
     */
    public final String createToken(final String userId) {
        return createToken(userId, defaultExpirationDelay);
    }

    /**
     * Creates a token for the given userId with an expiration delay.
     * @param userId userId
     * @param expirationDelay expiration delay
     * @return token
     */
    public final String createToken(final String userId, final long expirationDelay) {
        log.debug("Creating a token for user " + userId);

        // Set userId.
        Claims claims = Jwts.claims().setSubject(userId);

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
    public final Optional<String> getUserId(final String token) {
        log.debug("Getting user id for token " + token);
        try {
            // Getting data from the claims.
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            // Checking that the token is not expired.
            if (claims.getBody().getExpiration().before(new Date())) {
                log.debug("Token " + token + " expired");
                return Optional.empty();
            } else {
                // Return the userId.
                String userId = claims.getBody().getSubject();
                log.debug("Token " + token + " is from user " + userId);
                return Optional.of(userId);
            }
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid token : " + token);
            return Optional.empty();
        }
    }

}
