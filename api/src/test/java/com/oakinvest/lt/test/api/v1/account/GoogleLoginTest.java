package com.oakinvest.lt.test.api.v1.account;

import com.jayway.jsonpath.JsonPath;
import com.oakinvest.lt.domain.Account;
import com.oakinvest.lt.test.util.api.APITest;
import com.oakinvest.lt.test.util.authentication.GoogleRefreshToken;
import org.junit.Assert;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.oakinvest.lt.test.util.data.TestAccounts.GOOGLE_ACCOUNT_1;
import static com.oakinvest.lt.test.util.data.TestAccounts.GOOGLE_ACCOUNT_2;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Google login test.
 */
public class GoogleLoginTest extends APITest {

    @Override
    public void authenticationTest() throws Exception {
        // No google token provided.
        final MvcResult mvcResult = getMvc().perform(get(GOOGLE_LOGIN_URL)
                .contentType(APPLICATION_JSON_UTF8))
                .andReturn();
        assertNotEquals("Login not allowed", HttpStatus.FORBIDDEN, mvcResult.getResponse().getStatus());
    }

    @Override
    public void validDataTest() throws Exception {
        // No google token provided.
        getMvc().perform(get(GOOGLE_LOGIN_URL)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Google Id token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy string.
        getMvc().perform(get(GOOGLE_LOGIN_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .param("googleIdToken", "toto"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid Google Id token : toto"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Invalid google token provided (expired).
        String user1InvalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        getMvc().perform(get(GOOGLE_LOGIN_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .param("googleIdToken", user1InvalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid Google Id token : " + user1InvalidToken))
                .andExpect(jsonPath("errors", hasSize(0)));
    }

    @Override
    public void businessLogicTest() throws Exception {
        // Valid account token (loose.touch.test.1@gmail.com).
        Optional<GoogleRefreshToken> user1GoogleToken = getGoogleTokenRetriever().getIdToken(GOOGLE_ACCOUNT_1);
        if (user1GoogleToken.isPresent()) {
            // Check that there is no account in the database.
            assertEquals("There are users in the database", 0, accountsCount());

            // Check that the account doesn't exists yet.
            assertFalse("Account 1 already exists", getAccountRepository().findAccountByGoogleUsername(GOOGLE_ACCOUNT_1.getEmail()).isPresent());

            // First login.
            MvcResult result = getMvc().perform(get(GOOGLE_LOGIN_URL)
                    .param("googleIdToken", user1GoogleToken.get().getIdToken())
                    .param("googleAccessToken", user1GoogleToken.get().getAccessToken()))
                    .andExpect(jsonPath("idToken").isNotEmpty())
                    .andExpect(jsonPath("firstName").value(GOOGLE_ACCOUNT_1.getFirstName()))
                    .andExpect(jsonPath("lastName").value(GOOGLE_ACCOUNT_1.getLastName()))
                    .andExpect(jsonPath("email").value(GOOGLE_ACCOUNT_1.getEmail()))
                    .andExpect(jsonPath("pictureUrl").isString())
                    .andExpect(jsonPath("newAccount").value(true))
                    .andExpect(status().isOk())
                    .andReturn();
            String looseTouchUser1Token1 = JsonPath.parse(result.getResponse().getContentAsString()).read("idToken").toString();
//            System.out.println("==> " + looseTouchUser1Token1);
//            System.exit(-1);

            // Check that the account now exists and that the token is correct.
            Optional<Account> u1 = getAccountRepository().findAccountByGoogleUsername(GOOGLE_ACCOUNT_1.getEmail());
            Assert.assertTrue("Account 1 doesn't exists", u1.isPresent());
            assertEquals("There are too many users in the database", 1, accountsCount());
            Assert.assertTrue("Token for account 1 is not valid", getLooseTouchTokenProvider().getAccountId(looseTouchUser1Token1).isPresent());
            assertEquals("Token for account 1 doesn't have the good ID", u1.get().getId(), getLooseTouchTokenProvider().getAccountId(looseTouchUser1Token1).get());

            Thread.sleep(TimeUnit.SECONDS.toMillis(1));

            // Second login.
            result = getMvc().perform(get(GOOGLE_LOGIN_URL)
                    .contentType(APPLICATION_JSON_UTF8)
                    .param("googleIdToken", user1GoogleToken.get().getIdToken()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("idToken").isNotEmpty())
                    .andExpect(jsonPath("firstName").value(GOOGLE_ACCOUNT_1.getFirstName()))
                    .andExpect(jsonPath("lastName").value(GOOGLE_ACCOUNT_1.getLastName()))
                    .andExpect(jsonPath("email").value(GOOGLE_ACCOUNT_1.getEmail()))
                    .andExpect(jsonPath("pictureUrl").isString())
                    .andExpect(jsonPath("newAccount").value(false))
                    .andReturn();
            String looseTouchUser1Token2 = JsonPath.parse(result.getResponse().getContentAsString()).read("idToken").toString();

            // Check that the account is not created twice and that the token is new correct.
            assertNotEquals("Token are not different", looseTouchUser1Token1, looseTouchUser1Token2);
            u1 = getAccountRepository().findAccountByGoogleUsername(GOOGLE_ACCOUNT_1.getEmail());
            Assert.assertTrue("Account 1 doesn't exists", u1.isPresent());
            assertEquals("There are too many users in the database", 1, accountsCount());
            Assert.assertTrue("Token for account 1 is not valid", getLooseTouchTokenProvider().getAccountId(looseTouchUser1Token2).isPresent());
            assertEquals("Token for account 1 doesn't have the good ID", u1.get().getId(), getLooseTouchTokenProvider().getAccountId(looseTouchUser1Token2).get());
        } else {
            fail("Impossible to retrieve a token for account 1");
        }

        // Another account (loose.touch.test.2@gmail.com).
        Optional<GoogleRefreshToken> user2GoogleToken = getGoogleTokenRetriever().getIdToken(GOOGLE_ACCOUNT_2);
        if (user2GoogleToken.isPresent()) {
            // Check that there is one account in the database.
            assertEquals("There are two users in the database", 1, accountsCount());

            // Check that the account doesn't exists yet.
            assertFalse("Account 2 already exists", getAccountRepository().findAccountByGoogleUsername(GOOGLE_ACCOUNT_2.getEmail()).isPresent());

            // First login.
            MvcResult result = getMvc().perform(get(GOOGLE_LOGIN_URL)
                    .contentType(APPLICATION_JSON_UTF8)
                    .param("googleIdToken", user2GoogleToken.get().getIdToken())
                    .param("googleAccessToken", user2GoogleToken.get().getAccessToken()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("idToken").isNotEmpty())
                    .andExpect(jsonPath("firstName").value(GOOGLE_ACCOUNT_2.getFirstName()))
                    .andExpect(jsonPath("lastName").value(GOOGLE_ACCOUNT_2.getLastName()))
                    .andExpect(jsonPath("email").value(GOOGLE_ACCOUNT_2.getEmail()))
                    .andExpect(jsonPath("pictureUrl").isString())
                    .andExpect(jsonPath("newAccount").value(true))
                    .andReturn();
            String looseTouchUser2Token1 = JsonPath.parse(result.getResponse().getContentAsString()).read("idToken").toString();

            // Check that the account now exists and that the token is correct.
            Optional<Account> u2 = getAccountRepository().findAccountByGoogleUsername(GOOGLE_ACCOUNT_2.getEmail());
            Assert.assertTrue("Account 2 doesn't exists", u2.isPresent());
            assertEquals("There are too many users in the database", 2, accountsCount());
            Assert.assertTrue("Token for account 2 is not valid", getLooseTouchTokenProvider().getAccountId(looseTouchUser2Token1).isPresent());
            assertEquals("Token for account 2 doesn't have the good ID", u2.get().getId(), getLooseTouchTokenProvider().getAccountId(looseTouchUser2Token1).get());

            Thread.sleep(TimeUnit.SECONDS.toMillis(1));

            // Second login.
            result = getMvc().perform(get(GOOGLE_LOGIN_URL)
                    .contentType(APPLICATION_JSON_UTF8)
                    .param("googleIdToken", user2GoogleToken.get().getIdToken()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("idToken").isNotEmpty())
                    .andExpect(jsonPath("firstName").value(GOOGLE_ACCOUNT_2.getFirstName()))
                    .andExpect(jsonPath("lastName").value(GOOGLE_ACCOUNT_2.getLastName()))
                    .andExpect(jsonPath("email").value(GOOGLE_ACCOUNT_2.getEmail()))
                    .andExpect(jsonPath("pictureUrl").isString())
                    .andExpect(jsonPath("newAccount").value(false))
                    .andReturn();
            String looseTouchUser2Token2 = JsonPath.parse(result.getResponse().getContentAsString()).read("idToken").toString();

            // Check that the account is not created twice and that the token is new correct.
            assertNotEquals("Token are not different", looseTouchUser2Token1, looseTouchUser2Token2);
            u2 = getAccountRepository().findAccountByGoogleUsername(GOOGLE_ACCOUNT_2.getEmail());
            Assert.assertTrue("Account 2 doesn't exists", u2.isPresent());
            assertEquals("There are too many users in the database", 2, accountsCount());
            Assert.assertTrue("Token for account 2 is not valid", getLooseTouchTokenProvider().getAccountId(looseTouchUser2Token2).isPresent());
            assertEquals("Token for account 2 doesn't have the good ID", u2.get().getId(), getLooseTouchTokenProvider().getAccountId(looseTouchUser2Token2).get());
        } else {
            fail("Impossible to retrieve a token for account 2");
        }
    }

}
