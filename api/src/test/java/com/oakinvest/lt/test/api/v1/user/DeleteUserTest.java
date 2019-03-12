package com.oakinvest.lt.test.api.v1.user;

import com.oakinvest.lt.test.util.api.APITest;
import org.junit.Ignore;

import java.util.Calendar;

import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_1;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_2;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_3;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_4;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_5;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_1;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_2;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Delete contact test.
 */
public class DeleteUserTest extends APITest {

    @Override
    public void authenticationTest() throws Exception {
        // No token provided.
        getMvc().perform(delete(DELETE_USER_URL)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Loose touch token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy bearer.
        getMvc().perform(delete(DELETE_USER_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid loose touch token : invalidToken"))
                .andExpect(jsonPath("errors", hasSize(0)));
    }

    @Ignore("Impossible that user id is not set")
    @Override
    public void validDataTest() {

    }

    @Override
    public void businessLogicTest() throws Exception {
        // User creation.
        assertEquals(0, getUserRepository().count());
        assertEquals(0, getContactRepository().count());
        final String looseToucheTokenForUser1 = getLooseToucheToken(GOOGLE_USER_1);
        final String looseToucheTokenForUser2 = getLooseToucheToken(GOOGLE_USER_2);

        // Test data.
        // User 1 / contact 1.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());
        // User 1 / contact 2.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isCreated());
        // User 1 / contact 3.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_3.toDTO())))
                .andExpect(status().isCreated());
        // User 1 / contact 4.
        Calendar date = Calendar.getInstance();
        date.add(DATE, 4);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_4.toDTO())))
                .andExpect(status().isCreated());
        // User 1 / Contact 5.
        date = Calendar.getInstance();
        date.add(MONTH, 5);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_5.toDTO())))
                .andExpect(status().isCreated());
        // User 2 / Contact 1.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());
        // User 2 / Contact 2.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(getMapper().writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isCreated());
        assertEquals(2, getUserRepository().count());
        assertEquals(7, getContactRepository().count());

        // Delete user 1.
        getMvc().perform(delete(DELETE_USER_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1))
                .andExpect(status().isOk());
        assertEquals(1, getUserRepository().count());
        assertEquals(2, getContactRepository().count());

        // Delete user 2.
        getMvc().perform(delete(DELETE_USER_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2))
                .andExpect(status().isOk());
        assertEquals(0, getUserRepository().count());
        assertEquals(0, getContactRepository().count());
    }

}
