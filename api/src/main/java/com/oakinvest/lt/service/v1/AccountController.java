package com.oakinvest.lt.service.v1;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.oakinvest.lt.authentication.google.GoogleTokenVerifier;
import com.oakinvest.lt.authentication.loosetouch.AuthenticatedAccount;
import com.oakinvest.lt.authentication.loosetouch.LooseTouchTokenProvider;
import com.oakinvest.lt.domain.Account;
import com.oakinvest.lt.dto.util.GoogleUserInfoDTO;
import com.oakinvest.lt.dto.v1.AccountDTO;
import com.oakinvest.lt.repository.AccountRepository;
import com.oakinvest.lt.repository.ContactRepository;
import com.oakinvest.lt.util.error.LooseTouchException;
import com.oakinvest.lt.util.mapper.LooseTouchMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.oakinvest.lt.authentication.loosetouch.AccountAuthentication.GOOGLE;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.api_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;

/**
 * Account controller.
 */
@SuppressWarnings("unused")
@RestController
public class AccountController implements AccountAPI {

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
    private final GoogleTokenVerifier googleTokenVerifier;

    /**
     * JWT Token provider.
     */
    private final LooseTouchTokenProvider looseTouchTokenProvider;

    /**
     * Account repository.
     */
    private final AccountRepository accountRepository;

    /**
     * Contact repository.
     */
    private final ContactRepository contactRepository;

    /**
     * Constructor.
     *
     * @param newGoogleTokenVerifier     google token retriever
     * @param newLooseTouchTokenProvider loose touch token provider
     * @param newAccountRepository       account repository
     * @param newContactRepository       contact repository
     */
    public AccountController(final GoogleTokenVerifier newGoogleTokenVerifier,
                             final LooseTouchTokenProvider newLooseTouchTokenProvider,
                             final AccountRepository newAccountRepository,
                             final ContactRepository newContactRepository) {
        this.googleTokenVerifier = newGoogleTokenVerifier;
        this.looseTouchTokenProvider = newLooseTouchTokenProvider;
        this.accountRepository = newAccountRepository;
        this.contactRepository = newContactRepository;
    }

    @Override
    public final AccountDTO googleLogin(final String googleIdToken, final String googleAccessToken) {

        // Google Id token missing.
        if (googleIdToken == null) {
            throw new LooseTouchException(authentication_error, "Google Id token missing");
        }

        // Token verification.
        Optional<GoogleIdToken.Payload> payload = googleTokenVerifier.verifyToken(googleIdToken);
        if (!payload.isPresent()) {
            // Dummy or invalid token.
            throw new LooseTouchException(authentication_error, "Invalid Google Id token : " + googleIdToken);
        } else {
            // It's a valid token.

            // Search for the account email address in the database.
            Optional<Account> accountInDatabase = accountRepository.findAccountByGoogleUsername(payload.get().getEmail());
            Account account;

            if (accountInDatabase.isPresent()) {
                // if the account in the database, we return a token.
                account = accountInDatabase.get();

                // Return the profile with the id token.
                AccountDTO u = LooseTouchMapper.INSTANCE.accountToAccountDTO(account);
                u.setIdToken(looseTouchTokenProvider.createToken(account.getId()));
                return u;
            } else {
                // If not, we createContact it first and then, we return a token.

                // Account data
                String firstName;
                String lastName;
                String email;
                String imageUrl;

                if (payload.get().get("given_name") != null) {
                    // If account data are available in the id token.
                    firstName = (String) payload.get().get("given_name");
                    lastName = (String) payload.get().get("family_name");
                    email = payload.get().getEmail();
                    imageUrl = (String) payload.get().get("picture");
                } else {
                    // else we search them via google account info.
                    GoogleUserInfoDTO googleUserInfo = getGoogleUserInfo(googleAccessToken);
                    firstName = googleUserInfo.getGivenName();
                    lastName = googleUserInfo.getFamilyName();
                    email = googleUserInfo.getEmail();
                    imageUrl = googleUserInfo.getPicture();
                }
                account = new Account(firstName, lastName, email, imageUrl, GOOGLE);
                accountRepository.save(account);

                // Return the profile with the id token.
                AccountDTO u = LooseTouchMapper.INSTANCE.accountToAccountDTO(account);
                u.setIdToken(looseTouchTokenProvider.createToken(account.getId()));
                u.setNewAccount(true);
                return u;
            }
        }

    }

    @Override
    public final AccountDTO getProfile(final AuthenticatedAccount authenticatedAccount) {
        return accountRepository.getAccount(authenticatedAccount.getAccountId())
                .map(account -> {
                    AccountDTO u = LooseTouchMapper.INSTANCE.accountToAccountDTO(account);
                    u.setIdToken(looseTouchTokenProvider.createToken(account.getId()));
                    return u;
                })
                .orElseThrow(() -> new LooseTouchException(api_error, "Account " + authenticatedAccount.getAccountId() + " not found"));
    }

    @Override
    public final void delete(final AuthenticatedAccount authenticatedAccount) {
        // Delete all contacts.
        contactRepository.deleteAllContactsOfAccount(authenticatedAccount.getAccountId());
        // Delete account.
        accountRepository.delete(authenticatedAccount.getAccountId());
    }

    /**
     * getContact google account info.
     *
     * @param googleAccessToken google access token
     * @return google account info
     */
    @SuppressWarnings("WhitespaceAround")
    private GoogleUserInfoDTO getGoogleUserInfo(final String googleAccessToken) {
        // Calling google.
        return new RestTemplate().getForObject("https://www.googleapis.com/oauth2/v2/userinfo?access_token={access_token}", GoogleUserInfoDTO.class, googleAccessToken);
    }

}
