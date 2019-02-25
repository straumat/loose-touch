package com.oakinvest.lt.service.v1;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.oakinvest.lt.authentication.google.GoogleTokenVerifier;
import com.oakinvest.lt.authentication.loosetouch.LooseTouchTokenProvider;
import com.oakinvest.lt.domain.User;
import com.oakinvest.lt.repository.UserRepository;
import com.oakinvest.lt.util.error.LooseTouchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;

/**
 * Login controller.
 */
@RestController
public class LoginController implements LoginAPI {

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
        Optional<GoogleIdToken.Payload> token = googleTokenVerifier.verifyToken(googleIdToken);
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
                String firstName = (String) token.get().get("given_name");
                String lastName = (String) token.get().get("family_name");
                String email = token.get().getEmail();
                String imageUrl = (String) token.get().get("picture");
                User u = new User(firstName, lastName, email, imageUrl);
                userRepository.save(u);
                return looseTouchTokenProvider.createToken(u.getId());
            }
        }

    }

}
