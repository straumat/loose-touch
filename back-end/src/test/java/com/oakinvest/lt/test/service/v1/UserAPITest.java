package com.oakinvest.lt.test.service.v1;

import com.jayway.jsonpath.JsonPath;
import com.oakinvest.lt.test.util.junit.JUnitHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static com.oakinvest.lt.configuration.Application.LOCAL_DYNAMODB_ENVIRONMENT;
import static com.oakinvest.lt.test.util.authentication.GoogleTestUsers.USER_1;
import static com.oakinvest.lt.test.util.authentication.GoogleTestUsers.USER_2;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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
     * Test get profile.
     */
    @Test
    public void getProfileTest() throws Exception {
        // No google token provided.
        getMvc().perform(get(GET_PROFILE_URL))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Loose touch token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy bearer.
        getMvc().perform(get(GET_PROFILE_URL)
                .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid loose touch token : invalidToken"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Getting user 1 profile.
        MvcResult result1 = getMvc().perform(get(GET_PROFILE_URL)
                .header("Authorization", "Bearer " + getLooseToucheToken(USER_1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idToken").isNotEmpty())
                .andExpect(jsonPath("firstName").value(USER_1.getFirstName()))
                .andExpect(jsonPath("lastName").value(USER_1.getLastName()))
                .andExpect(jsonPath("email").value(USER_1.getEmail()))
                .andExpect(jsonPath("pictureUrl").isString())
                .andReturn();

        // Check that the returned token is ok.
        String looseTouchUser1Token = JsonPath.parse(result1.getResponse().getContentAsString()).read("idToken").toString();
        assertTrue("Invalid token", getLooseTouchTokenProvider().getUserId(looseTouchUser1Token).isPresent());
        assertTrue("Invalid user", getUserRepository().getUser(getLooseTouchTokenProvider().getUserId(looseTouchUser1Token).get()).isPresent());

        // Getting user 2 profile.
        MvcResult result2 = getMvc().perform(get(GET_PROFILE_URL)
                .header("Authorization", "Bearer " + getLooseToucheToken(USER_2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idToken").isNotEmpty())
                .andExpect(jsonPath("firstName").value(USER_2.getFirstName()))
                .andExpect(jsonPath("lastName").value(USER_2.getLastName()))
                .andExpect(jsonPath("email").value(USER_2.getEmail()))
                .andExpect(jsonPath("pictureUrl").isString())
                .andReturn();

        // Check that the returned token is ok.
        String looseTouchUser2Token = JsonPath.parse(result2.getResponse().getContentAsString()).read("idToken").toString();
        assertTrue("Invalid token", getLooseTouchTokenProvider().getUserId(looseTouchUser2Token).isPresent());
        assertTrue("Invalid user", getUserRepository().getUser(getLooseTouchTokenProvider().getUserId(looseTouchUser2Token).get()).isPresent());
    }

}
