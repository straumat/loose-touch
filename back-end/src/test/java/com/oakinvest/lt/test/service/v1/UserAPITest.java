package com.oakinvest.lt.test.service.v1;

import com.oakinvest.lt.authentication.loosetouch.LooseTouchTokenProvider;
import com.oakinvest.lt.domain.User;
import com.oakinvest.lt.repository.UserRepository;
import com.oakinvest.lt.test.util.authentication.GoogleTokenRetriever;
import com.oakinvest.lt.test.util.junit.JUnitHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static com.oakinvest.lt.test.util.authentication.GoogleTokenRetrieverUser.USER_1;
import static com.oakinvest.lt.test.util.authentication.GoogleTokenRetrieverUser.USER_2;
import static com.oakinvest.lt.util.rest.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.rest.LooseTouchErrorType.invalid_request_error;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * User API Test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserAPITest extends JUnitHelper {

    /**
     * User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Google token retriever.
     */
    @Autowired
    private GoogleTokenRetriever googleTokenRetriever;

    /**
     * Loose touch token provider.
     */
    @Autowired
    private LooseTouchTokenProvider looseTouchTokenProvider;

    /**
     * Mock mvc.
     */
    @Autowired
    private MockMvc mvc;

    /**
     * Ping test.
     */
    @Test
    public void googleLoginTest() throws Exception {
        // No google token provided.
        mvc.perform(get("/v1/login/google"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Google Id token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy string.
        mvc.perform(get("/v1/login/google")
                .param("googleIdToken", "toto"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid Google Id token : toto"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Invalid google token provided (expired).
        String user1InvalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        mvc.perform(get("/v1/login/google")
                .param("googleIdToken", user1InvalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid Google Id token : " + user1InvalidToken))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Valid user token (loose.touch.test.1@gmail.com).
        Optional<String> user1GoogleToken = googleTokenRetriever.getIdToken(USER_1);
        if (user1GoogleToken.isPresent()) {
            // Check that there is no user in the database.
            assertEquals("There are users in the database", 0, userRepository.count());

            // Check that the user doesn't exists yet.
            assertFalse("User 1 already exists", userRepository.findUserByGoogleUsername(USER_1.getEmail()).isPresent());

            // First login.
            MvcResult result = mvc.perform(get("/v1/login/google")
                    .param("googleIdToken", user1GoogleToken.get()))
                    .andExpect(status().isOk())
                    .andReturn();
            String looseTouchUser1Token1 = result.getResponse().getContentAsString();

            // Check that the user now exists and that the token is correct.
            Optional<User> u1 = userRepository.findUserByGoogleUsername(USER_1.getEmail());
            assertTrue("User 1 doesn't exists", u1 .isPresent());
            assertEquals("There are too many users in the database", 1, userRepository.count());
            assertTrue("Token for user 1 is not valid", looseTouchTokenProvider.getUserId(looseTouchUser1Token1).isPresent());
            assertEquals("Token for user 1 doesn't have the good ID", u1.get().getId(), looseTouchTokenProvider.getUserId(looseTouchUser1Token1).get());

            // Second login.
            result = mvc.perform(get("/v1/login/google")
                    .param("googleIdToken", user1GoogleToken.get()))
                    .andExpect(status().isOk())
                    .andReturn();
            String looseTouchUser1Token2 = result.getResponse().getContentAsString();

            // Check that the user is not created twice and that the token is new correct.
            assertNotEquals("Token are not different", looseTouchUser1Token1, looseTouchUser1Token2);
            u1 = userRepository.findUserByGoogleUsername(USER_1.getEmail());
            assertTrue("User 1 doesn't exists", u1 .isPresent());
            assertEquals("There are too many users in the database", 1, userRepository.count());
            assertTrue("Token for user 1 is not valid", looseTouchTokenProvider.getUserId(looseTouchUser1Token2).isPresent());
            assertEquals("Token for user 1 doesn't have the good ID", u1.get().getId(), looseTouchTokenProvider.getUserId(looseTouchUser1Token2).get());
        } else {
            fail("Impossible to retrieve a token for user 1");
        }

        // Another user.
        Optional<String> user2GoogleToken = googleTokenRetriever.getIdToken(USER_2);
        if (user2GoogleToken.isPresent()) {
            // Check that there is one user in the database.
            assertEquals("There are two users in the database", 1, userRepository.count());

            // Check that the user doesn't exists yet.
            assertFalse("User 2 already exists", userRepository.findUserByGoogleUsername(USER_2.getEmail()).isPresent());

            // First login.
            MvcResult result = mvc.perform(get("/v1/login/google")
                    .param("googleIdToken", user2GoogleToken.get()))
                    .andExpect(status().isOk())
                    .andReturn();
            String looseTouchUser2Token1 = result.getResponse().getContentAsString();

            // Check that the user now exists and that the token is correct.
            Optional<User> u2 = userRepository.findUserByGoogleUsername(USER_2.getEmail());
            assertTrue("User 2 doesn't exists", u2 .isPresent());
            assertEquals("There are too many users in the database", 2, userRepository.count());
            assertTrue("Token for user 2 is not valid", looseTouchTokenProvider.getUserId(looseTouchUser2Token1).isPresent());
            assertEquals("Token for user 2 doesn't have the good ID", u2.get().getId(), looseTouchTokenProvider.getUserId(looseTouchUser2Token1).get());

            // Second login.
            result = mvc.perform(get("/v1/login/google")
                    .param("googleIdToken", user1GoogleToken.get()))
                    .andExpect(status().isOk())
                    .andReturn();
            String looseTouchUser2Token2 = result.getResponse().getContentAsString();

            // Check that the user is not created twice and that the token is new correct.
            assertNotEquals("Token are not different", looseTouchUser2Token1, looseTouchUser2Token2);
            u2 = userRepository.findUserByGoogleUsername(USER_1.getEmail());
            assertTrue("User 2 doesn't exists", u2 .isPresent());
            assertEquals("There are too many users in the database", 2, userRepository.count());
            assertTrue("Token for user 2 is not valid", looseTouchTokenProvider.getUserId(looseTouchUser2Token2).isPresent());
            assertEquals("Token for user 2 doesn't have the good ID", u2.get().getId(), looseTouchTokenProvider.getUserId(looseTouchUser2Token2).get());
        } else {
            fail("Impossible to retrieve a token for user 1");
        }

    }

}
