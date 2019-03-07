package com.oakinvest.lt.test.api.v1.contact;

import com.oakinvest.lt.dto.v1.ContactDTO;
import com.oakinvest.lt.test.util.api.APITest;
import org.junit.Ignore;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_1;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_2;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_3;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_4;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_1;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_2;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
import static java.util.Calendar.DATE;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Contacts to reach test.
 */
public class ContactsToReachTest extends APITest {

    @Override
    public void authenticationTest() throws Exception {
        // No token provided.
        getMvc().perform(get(CONTACT_URL + "/toReach")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Loose touch token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy bearer.
        getMvc().perform(get(CONTACT_URL + "/toReach")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid loose touch token : invalidToken"))
                .andExpect(jsonPath("errors", hasSize(0)));
    }

    @Ignore("No parameters required for this method")
    @Override
    public void validDataTest() throws Exception {

    }

    @Override
    public void businessLogicTest() throws Exception {
        // Configuration.
        final String looseToucheTokenForUser1 = getLooseToucheToken(GOOGLE_USER_1);
        final String looseToucheTokenForUser2 = getLooseToucheToken(GOOGLE_USER_2);
        ContactDTO c;

        // =============================================================================================================
        // Test data.
        // User 1 / contact 1/
        c = CONTACT_1.toDTO();
        c.setContactDueDate(new GregorianCalendar(2018, Calendar.DECEMBER, 31, 13, 24, 56));
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(c)))
                .andExpect(status().isCreated());
        // User 1 / contact 2.
        c = CONTACT_2.toDTO();
        c.setContactDueDate(new GregorianCalendar(2058, Calendar.DECEMBER, 31, 13, 24, 56));
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(c)))
                .andExpect(status().isCreated());
        // User 1 / contact 3.
        c = CONTACT_3.toDTO();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(DATE, 2);
        c.setContactDueDate(tomorrow);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(c)))
                .andExpect(status().isCreated());
        // User 1 / contact 4.
        c = CONTACT_4.toDTO();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(DATE, -2);
        c.setContactDueDate(yesterday);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(c)))
                .andExpect(status().isCreated());

        // User 2 / Contact 1.
        c = CONTACT_1.toDTO();
        c.setContactDueDate(new GregorianCalendar(2018, Calendar.DECEMBER, 31, 13, 24, 56));
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(getMapper().writeValueAsString(c)))
                .andExpect(status().isCreated());
        // User 2 / Contact 2.
        c = CONTACT_2.toDTO();
        c.setContactDueDate(new GregorianCalendar(2058, Calendar.DECEMBER, 31, 13, 24, 56));
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(getMapper().writeValueAsString(c)))
                .andExpect(status().isCreated());
        assertEquals(2, getUserRepository().count());
        assertEquals(6, getContactRepository().count());

        // =============================================================================================================
        // Getting the list of contacts to reach.
        getMvc().perform(get(CONTACT_URL + "/toReach")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1))
                .andExpect(jsonPath("$", hasSize(2)))
                // First result.
                .andExpect(jsonPath("$[0].email").value(CONTACT_1.getEmail()))
                // Second result.
                .andExpect(jsonPath("$[1].email").value(CONTACT_4.getEmail()));

        getMvc().perform(get(CONTACT_URL + "/toReach")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2))
                .andExpect(jsonPath("$", hasSize(1)))
                // First result.
                .andExpect(jsonPath("$[0].email").value(CONTACT_1.getEmail()));

        // =============================================================================================================
        // Set CONTACT_1 as contacted and see if it disappears.
        getMvc().perform(get(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/contacted")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1))
                .andExpect(status().isOk());
        getMvc().perform(get(CONTACT_URL + "/toReach")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1))
                .andExpect(jsonPath("$", hasSize(1)))
                // First result.
                .andExpect(jsonPath("$[0].email").value(CONTACT_4.getEmail()));

        getMvc().perform(get(CONTACT_URL + "/toReach")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2))
                .andExpect(jsonPath("$", hasSize(1)))
                // First result.
                .andExpect(jsonPath("$[0].email").value(CONTACT_1.getEmail()));
    }

}
