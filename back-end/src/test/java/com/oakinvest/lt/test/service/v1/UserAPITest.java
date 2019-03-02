package com.oakinvest.lt.test.service.v1;

import com.jayway.jsonpath.JsonPath;
import com.oakinvest.lt.domain.User;
import com.oakinvest.lt.test.util.authentication.GoogleRefreshToken;
import com.oakinvest.lt.test.util.junit.JUnitHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.oakinvest.lt.configuration.Application.LOCAL_DYNAMODB_ENVIRONMENT;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_1;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_2;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * User API Test.
 */
@ActiveProfiles(LOCAL_DYNAMODB_ENVIRONMENT)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserAPITest extends JUnitHelper {

    /**
     * Get profile URL.
     */
    private static final String GET_PROFILE_URL = "/v1/profile";

    /**
     * Ping test.
     */
    @Test
    public void googleLoginTest() throws Exception {
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

        // Valid user token (loose.touch.test.1@gmail.com).
        Optional<GoogleRefreshToken> user1GoogleToken = getGoogleTokenRetriever().getIdToken(GOOGLE_USER_1);
        if (user1GoogleToken.isPresent()) {
            // Check that there is no user in the database.
            assertEquals("There are users in the database", 0, getUserRepository().count());

            // Check that the user doesn't exists yet.
            assertFalse("User 1 already exists", getUserRepository().findUserByGoogleUsername(GOOGLE_USER_1.getEmail()).isPresent());

            // First login.
            MvcResult result = getMvc().perform(get(GOOGLE_LOGIN_URL)
                    .param("googleIdToken", user1GoogleToken.get().getIdToken())
                    .param("googleAccessToken", user1GoogleToken.get().getAccessToken()))
                    .andExpect(jsonPath("idToken").isNotEmpty())
                    .andExpect(jsonPath("firstName").value(GOOGLE_USER_1.getFirstName()))
                    .andExpect(jsonPath("lastName").value(GOOGLE_USER_1.getLastName()))
                    .andExpect(jsonPath("email").value(GOOGLE_USER_1.getEmail()))
                    .andExpect(jsonPath("pictureUrl").isString())
                    .andExpect(jsonPath("newAccount").value(true))
                    .andExpect(status().isOk())
                    .andReturn();
            String looseTouchUser1Token1 = JsonPath.parse(result.getResponse().getContentAsString()).read("idToken").toString();

            // Check that the user now exists and that the token is correct.
            Optional<User> u1 = getUserRepository().findUserByGoogleUsername(GOOGLE_USER_1.getEmail());
            Assert.assertTrue("User 1 doesn't exists", u1.isPresent());
            assertEquals("There are too many users in the database", 1, getUserRepository().count());
            Assert.assertTrue("Token for user 1 is not valid", getLooseTouchTokenProvider().getUserId(looseTouchUser1Token1).isPresent());
            assertEquals("Token for user 1 doesn't have the good ID", u1.get().getId(), getLooseTouchTokenProvider().getUserId(looseTouchUser1Token1).get());

            Thread.sleep(TimeUnit.SECONDS.toMillis(1));

            // Second login.
            result = getMvc().perform(get(GOOGLE_LOGIN_URL)
                    .contentType(APPLICATION_JSON_UTF8)
                    .param("googleIdToken", user1GoogleToken.get().getIdToken()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("idToken").isNotEmpty())
                    .andExpect(jsonPath("firstName").value(GOOGLE_USER_1.getFirstName()))
                    .andExpect(jsonPath("lastName").value(GOOGLE_USER_1.getLastName()))
                    .andExpect(jsonPath("email").value(GOOGLE_USER_1.getEmail()))
                    .andExpect(jsonPath("pictureUrl").isString())
                    .andExpect(jsonPath("newAccount").value(false))
                    .andReturn();
            String looseTouchUser1Token2 = JsonPath.parse(result.getResponse().getContentAsString()).read("idToken").toString();

            // Check that the user is not created twice and that the token is new correct.
            assertNotEquals("Token are not different", looseTouchUser1Token1, looseTouchUser1Token2);
            u1 = getUserRepository().findUserByGoogleUsername(GOOGLE_USER_1.getEmail());
            Assert.assertTrue("User 1 doesn't exists", u1.isPresent());
            assertEquals("There are too many users in the database", 1, getUserRepository().count());
            Assert.assertTrue("Token for user 1 is not valid", getLooseTouchTokenProvider().getUserId(looseTouchUser1Token2).isPresent());
            assertEquals("Token for user 1 doesn't have the good ID", u1.get().getId(), getLooseTouchTokenProvider().getUserId(looseTouchUser1Token2).get());
        } else {
            fail("Impossible to retrieve a token for user 1");
        }

        // Another user (loose.touch.test.2@gmail.com)..
        Optional<GoogleRefreshToken> user2GoogleToken = getGoogleTokenRetriever().getIdToken(GOOGLE_USER_2);
        if (user2GoogleToken.isPresent()) {
            // Check that there is one user in the database.
            assertEquals("There are two users in the database", 1, getUserRepository().count());

            // Check that the user doesn't exists yet.
            assertFalse("User 2 already exists", getUserRepository().findUserByGoogleUsername(GOOGLE_USER_2.getEmail()).isPresent());

            // First login.
            MvcResult result = getMvc().perform(get(GOOGLE_LOGIN_URL)
                    .contentType(APPLICATION_JSON_UTF8)
                    .param("googleIdToken", user2GoogleToken.get().getIdToken())
                    .param("googleAccessToken", user2GoogleToken.get().getAccessToken()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("idToken").isNotEmpty())
                    .andExpect(jsonPath("firstName").value(GOOGLE_USER_2.getFirstName()))
                    .andExpect(jsonPath("lastName").value(GOOGLE_USER_2.getLastName()))
                    .andExpect(jsonPath("email").value(GOOGLE_USER_2.getEmail()))
                    .andExpect(jsonPath("pictureUrl").isString())
                    .andExpect(jsonPath("newAccount").value(true))
                    .andReturn();
            String looseTouchUser2Token1 = JsonPath.parse(result.getResponse().getContentAsString()).read("idToken").toString();

            // Check that the user now exists and that the token is correct.
            Optional<User> u2 = getUserRepository().findUserByGoogleUsername(GOOGLE_USER_2.getEmail());
            Assert.assertTrue("User 2 doesn't exists", u2.isPresent());
            assertEquals("There are too many users in the database", 2, getUserRepository().count());
            Assert.assertTrue("Token for user 2 is not valid", getLooseTouchTokenProvider().getUserId(looseTouchUser2Token1).isPresent());
            assertEquals("Token for user 2 doesn't have the good ID", u2.get().getId(), getLooseTouchTokenProvider().getUserId(looseTouchUser2Token1).get());

            Thread.sleep(TimeUnit.SECONDS.toMillis(1));

            // Second login.
            result = getMvc().perform(get(GOOGLE_LOGIN_URL)
                    .contentType(APPLICATION_JSON_UTF8)
                    .param("googleIdToken", user2GoogleToken.get().getIdToken()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("idToken").isNotEmpty())
                    .andExpect(jsonPath("firstName").value(GOOGLE_USER_2.getFirstName()))
                    .andExpect(jsonPath("lastName").value(GOOGLE_USER_2.getLastName()))
                    .andExpect(jsonPath("email").value(GOOGLE_USER_2.getEmail()))
                    .andExpect(jsonPath("pictureUrl").isString())
                    .andExpect(jsonPath("newAccount").value(false))
                    .andReturn();
            String looseTouchUser2Token2 = JsonPath.parse(result.getResponse().getContentAsString()).read("idToken").toString();

            // Check that the user is not created twice and that the token is new correct.
            assertNotEquals("Token are not different", looseTouchUser2Token1, looseTouchUser2Token2);
            u2 = getUserRepository().findUserByGoogleUsername(GOOGLE_USER_2.getEmail());
            Assert.assertTrue("User 2 doesn't exists", u2.isPresent());
            assertEquals("There are too many users in the database", 2, getUserRepository().count());
            Assert.assertTrue("Token for user 2 is not valid", getLooseTouchTokenProvider().getUserId(looseTouchUser2Token2).isPresent());
            assertEquals("Token for user 2 doesn't have the good ID", u2.get().getId(), getLooseTouchTokenProvider().getUserId(looseTouchUser2Token2).get());
        } else {
            fail("Impossible to retrieve a token for user 1");
        }

    }

    /**
     * Test get profile.
     */
    @Test
    public void getProfileTest() throws Exception {
        // No google token provided.
        getMvc().perform(get(GET_PROFILE_URL)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Loose touch token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy bearer.
        getMvc().perform(get(GET_PROFILE_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid loose touch token : invalidToken"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Getting user 1 profile.
        MvcResult result1 = getMvc().perform(get(GET_PROFILE_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getLooseToucheToken(GOOGLE_USER_1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idToken").isNotEmpty())
                .andExpect(jsonPath("firstName").value(GOOGLE_USER_1.getFirstName()))
                .andExpect(jsonPath("lastName").value(GOOGLE_USER_1.getLastName()))
                .andExpect(jsonPath("email").value(GOOGLE_USER_1.getEmail()))
                .andExpect(jsonPath("pictureUrl").isString())
                .andExpect(jsonPath("newAccount").value(false))
                .andReturn();

        // Check that the returned token is ok.
        String looseTouchUser1Token = JsonPath.parse(result1.getResponse().getContentAsString()).read("idToken").toString();
        assertTrue("Invalid token", getLooseTouchTokenProvider().getUserId(looseTouchUser1Token).isPresent());
        assertTrue("Invalid user", getUserRepository().getUser(getLooseTouchTokenProvider().getUserId(looseTouchUser1Token).get()).isPresent());

        // Getting user 2 profile.
        MvcResult result2 = getMvc().perform(get(GET_PROFILE_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getLooseToucheToken(GOOGLE_USER_2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idToken").isNotEmpty())
                .andExpect(jsonPath("firstName").value(GOOGLE_USER_2.getFirstName()))
                .andExpect(jsonPath("lastName").value(GOOGLE_USER_2.getLastName()))
                .andExpect(jsonPath("email").value(GOOGLE_USER_2.getEmail()))
                .andExpect(jsonPath("pictureUrl").isString())
                .andExpect(jsonPath("newAccount").value(false))
                .andReturn();

        // Check that the returned token is ok.
        String looseTouchUser2Token = JsonPath.parse(result2.getResponse().getContentAsString()).read("idToken").toString();
        assertTrue("Invalid token", getLooseTouchTokenProvider().getUserId(looseTouchUser2Token).isPresent());
        assertTrue("Invalid user", getUserRepository().getUser(getLooseTouchTokenProvider().getUserId(looseTouchUser2Token).get()).isPresent());
    }

}
