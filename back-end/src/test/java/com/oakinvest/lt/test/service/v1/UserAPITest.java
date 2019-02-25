package com.oakinvest.lt.test.service.v1;

import com.oakinvest.lt.test.util.junit.JUnitHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.oakinvest.lt.test.util.authentication.GoogleTokenRetrieverUser.USER_1;
import static com.oakinvest.lt.test.util.authentication.GoogleTokenRetrieverUser.USER_2;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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
        getMvc().perform(get(GET_PROFILE_URL)
                .header("Authorization", "Bearer " + getLooseToucheToken(USER_1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("loose 1"))
                .andExpect(jsonPath("lastName").value("touch 1"))
                .andExpect(jsonPath("email").value("loose.touch.test.1@gmail.com"))
                .andExpect(jsonPath("pictureUrl").value("https://lh5.googleusercontent.com/-vTIMhyL9ePM/AAAAAAAAAAI/AAAAAAAAAAA/ACevoQPEHAQw-lr-v1PCh4yr9AsWWmrITQ/s96-c/photo.jpg"));

        // Getting user 2 profile.
        getMvc().perform(get(GET_PROFILE_URL)
                .header("Authorization", "Bearer " + getLooseToucheToken(USER_2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName").value("loose 2"))
                .andExpect(jsonPath("lastName").value("touch 2"))
                .andExpect(jsonPath("email").value("loose.touch.test.2@gmail.com"))
                .andExpect(jsonPath("pictureUrl").value("https://lh3.googleusercontent.com/-GxmjZPF4TI8/AAAAAAAAAAI/AAAAAAAAAAA/ACevoQMMdCafIH3Xd97DRl1nbQtbvuijAg/s96-c/photo.jpg"));
    }

}
