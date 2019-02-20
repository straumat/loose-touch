package com.oakinvest.lt.service.v1;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.oakinvest.lt.authentication.google.GoogleTokenVerifier;
import com.oakinvest.lt.authentication.loosetouch.LooseTouchTokenProvider;
import com.oakinvest.lt.domain.User;
import com.oakinvest.lt.repository.UserRepository;
import com.oakinvest.lt.util.rest.LooseTouchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.oakinvest.lt.util.rest.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.rest.LooseTouchErrorType.invalid_request_error;

/**
 * User controller.
 */
@RestController
public class UserController implements UserAPI {

    /**
     * User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Google token verifier.
     */
    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    /**
     * JWT Token provider.
     */
    @Autowired
    private LooseTouchTokenProvider looseTouchTokenProvider;

    /**
     * Application login for google (if the user doesn't exists, creates it).
     *
     * @param googleIdToken google ID token
     * @return loose touch token
     */
    @Override
    public final String googleLogin(final String googleIdToken) {

        // Google Id token missing.
        if (googleIdToken == null) {
            throw new LooseTouchException(invalid_request_error, "Google Id token missing");
        }

        // Token verification.
        Optional<Payload> token = googleTokenVerifier.verifyToken(googleIdToken);
        if (!token.isPresent()) {
            // Dummy or invalid token.
            throw new LooseTouchException(authentication_error, "Invalid Google Id token : " + googleIdToken);
        } else {
            // It's a valid token.

            // Search for the user email address in the database.
            Optional<User> user = userRepository.findUserByGoogleUsername(token.get().getEmail());

            if (user.isPresent()) {
                // if the user in the database, we return a token.
                return looseTouchTokenProvider.createToken(user.get().getId());
            } else {
                // If not, we create it first and then, we return a token.
                String firstName = (String) token.get().get("name");
                String lastName = (String) token.get().get("family_name");
                String email = token.get().getEmail();
                User u = new User(firstName, lastName, email);
                userRepository.save(u);
                return looseTouchTokenProvider.createToken(u.getId());
            }
        }
    }

}
