package com.oakinvest.lt.service.v1;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.oakinvest.lt.authentication.google.GoogleTokenVerifier;
import com.oakinvest.lt.authentication.loosetouch.AuthenticatedUser;
import com.oakinvest.lt.authentication.loosetouch.LooseTouchTokenProvider;
import com.oakinvest.lt.domain.User;
import com.oakinvest.lt.dto.util.GoogleUserInfoDTO;
import com.oakinvest.lt.dto.v1.UserDTO;
import com.oakinvest.lt.repository.ContactRepository;
import com.oakinvest.lt.repository.UserRepository;
import com.oakinvest.lt.util.error.LooseTouchException;
import com.oakinvest.lt.util.mapper.LooseTouchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.oakinvest.lt.authentication.loosetouch.UserAuthentication.GOOGLE;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.api_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;

/**
 * User controller.
 */
@RestController
public class UserController implements UserAPI {

    /**
     * Client id.
     */
    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    /**
     * Client secret.
     */
    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    /**
     * Access token url.
     */
    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;

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
     * User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Contact repository.
     */
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public final UserDTO googleLogin(final String googleIdToken, final String googleAccessToken) {

        // Google Id token missing.
        if (googleIdToken == null) {
            throw new LooseTouchException(invalid_request_error, "Google Id token missing");
        }

        // Token verification.
        Optional<GoogleIdToken.Payload> payload = googleTokenVerifier.verifyToken(googleIdToken);
        if (!payload.isPresent()) {
            // Dummy or invalid token.
            throw new LooseTouchException(authentication_error, "Invalid Google Id token : " + googleIdToken);
        } else {
            // It's a valid token.

            // Search for the user email address in the database.
            Optional<User> userInDatabase = userRepository.findUserByGoogleUsername(payload.get().getEmail());
            User user;

            if (userInDatabase.isPresent()) {
                // if the user in the database, we return a token.
                user = userInDatabase.get();

                // Return the profile with the id token.
                UserDTO u = LooseTouchMapper.INSTANCE.userToUserDTO(user);
                u.setIdToken(looseTouchTokenProvider.createToken(user.getId()));
                return u;
            } else {
                // If not, we createContact it first and then, we return a token.

                // User data
                String firstName;
                String lastName;
                String email;
                String imageUrl;

                if (payload.get().get("given_name") != null) {
                    // If user data are available in the id token.
                    firstName = (String) payload.get().get("given_name");
                    lastName = (String) payload.get().get("family_name");
                    email = payload.get().getEmail();
                    imageUrl = (String) payload.get().get("picture");
                } else {
                    // else we search them via google user info.
                    GoogleUserInfoDTO googleUserInfo = getGoogleUserInfo(googleAccessToken);
                    firstName = googleUserInfo.getGivenName();
                    lastName = googleUserInfo.getFamilyName();
                    email = googleUserInfo.getEmail();
                    imageUrl = googleUserInfo.getPicture();
                }
                user = new User(firstName, lastName, email, imageUrl, GOOGLE);
                userRepository.save(user);

                // Return the profile with the id token.
                UserDTO u = LooseTouchMapper.INSTANCE.userToUserDTO(user);
                u.setIdToken(looseTouchTokenProvider.createToken(user.getId()));
                u.setNewAccount(true);
                return u;
            }
        }

    }

    @Override
    public final UserDTO getProfile(final AuthenticatedUser authenticatedUser) {
        return userRepository.getUser(authenticatedUser.getUserId())
                .map(user -> {
                    UserDTO u = LooseTouchMapper.INSTANCE.userToUserDTO(user);
                    u.setIdToken(looseTouchTokenProvider.createToken(user.getId()));
                    return u;
                })
                .orElseThrow(() -> new LooseTouchException(api_error, "User " + authenticatedUser.getUserId() + " not found"));
    }

    @Override
    public final void delete(final AuthenticatedUser authenticatedUser) {
        // Delete all contacts.
        contactRepository.deleteAllContactsOfUser(authenticatedUser.getUserId());
        // Delete user.
        userRepository.delete(authenticatedUser.getUserId());
    }

    /**
     * getContact google user info.
     *
     * @param googleAccessToken google access token
     * @return google user info
     */
    @SuppressWarnings("WhitespaceAround")
    private GoogleUserInfoDTO getGoogleUserInfo(final String googleAccessToken) {
        // Headers.
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        headers.setAll(map);

        // Parameters.
        Map<String, String> payload = new HashMap<>();
        payload.put("access_token", googleAccessToken);

        // Calling google.
        HttpEntity<?> request = new HttpEntity<>(payload, headers);
        return new RestTemplate().getForObject("https://www.googleapis.com/oauth2/v2/userinfo?access_token={access_token}", GoogleUserInfoDTO.class, googleAccessToken);
    }

}
