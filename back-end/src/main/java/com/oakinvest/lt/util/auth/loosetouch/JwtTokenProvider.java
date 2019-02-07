package com.oakinvest.lt.util.auth.loosetouch;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
 * We are using JWT to create the token for our application.
 *
 * JWT is RFC 7519 standard for representing and sharing claims securely between communicating parties.
 */
@Component
public class JwtTokenProvider {

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
        try {
            // Getting data from the claims.
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            // Checking that the token is not expired.
            if (claims.getBody().getExpiration().before(new Date())) {
                return Optional.empty();
            } else {
                // Return the userId.
                return Optional.of(claims.getBody().getSubject());
            }
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
