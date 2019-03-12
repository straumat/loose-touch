package com.oakinvest.lt.test.api.v1.contact;

import com.oakinvest.lt.test.util.api.APITest;
import org.junit.Ignore;

import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_1;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_3;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_1;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_2;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Get contact test.
 */
public class GetContactTest extends APITest {

    @Override
    public void authenticationTest() throws Exception {
        // No token provided.
        getMvc().perform(get(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Loose touch token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy bearer.
        getMvc().perform(get(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid loose touch token : invalidToken"))
                .andExpect(jsonPath("errors", hasSize(0)));
    }

    @Ignore("If email is null, we have Request method 'GET' not supported")
    @Override
    public void validDataTest() {

    }

    @Override
    public void businessLogicTest() throws Exception {
        // Configuration.
        final String looseToucheTokenForUser1 = getLooseToucheToken(GOOGLE_USER_1);
        final String looseToucheTokenForUser2 = getLooseToucheToken(GOOGLE_USER_2);

        System.out.println("TOTO " + getMapper().writeValueAsString(CONTACT_3.toDTO()));

        // Contact 1 for user 1 should not exists.
        getMvc().perform(get(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1))
                .andExpect(status().isNotFound());

        // Create contact 1 for user 1.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());

        // Contact 1 for user 1 should exists.
        getMvc().perform(get(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(CONTACT_1.getEmail()))
                .andExpect(jsonPath("firstName").value(CONTACT_1.getFirstName()))
                .andExpect(jsonPath("lastName").value(CONTACT_1.getLastName()))
                .andExpect(jsonPath("notes").value(CONTACT_1.getNotes()))
                .andExpect(jsonPath("contactRecurrenceType").value(CONTACT_1.getContactRecurrenceType()))
                .andExpect(jsonPath("contactRecurrenceValue").value(CONTACT_1.getContactRecurrenceValue()))
                .andExpect(jsonPath("contactRecurrenceValue").value(CONTACT_1.getContactRecurrenceValue()))
                .andExpect(jsonPath("contactDueDate").value("31/12/2019"));

        // Contact 1 for user 2 should not exists.
        getMvc().perform(get(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2))
                .andExpect(status().isNotFound());
    }

}
