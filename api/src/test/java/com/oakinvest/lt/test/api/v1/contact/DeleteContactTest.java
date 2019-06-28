package com.oakinvest.lt.test.api.v1.contact;

import com.oakinvest.lt.test.util.api.APITest;

import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_1;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_2;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_3;
import static com.oakinvest.lt.test.util.data.TestAccounts.GOOGLE_ACCOUNT_1;
import static com.oakinvest.lt.test.util.data.TestAccounts.GOOGLE_ACCOUNT_2;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
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
public class DeleteContactTest extends APITest {

    @Override
    public void authenticationTest() throws Exception {
        // No token provided.
        getMockMvc().perform(delete(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Loose touch token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy bearer.
        getMockMvc().perform(delete(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid loose touch token : invalidToken"))
                .andExpect(jsonPath("errors", hasSize(0)));
    }

    @Override
    public void validDataTest() throws Exception {
        // Trying to delete a non existing contact.
        getMockMvc().perform(delete(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getLooseToucheToken(GOOGLE_ACCOUNT_1)))
                .andExpect(status().isNotFound());
    }

    @Override
    public void businessLogicTest() throws Exception {
        // Configuration.
        final String looseToucheTokenForAccount1 = getLooseToucheToken(GOOGLE_ACCOUNT_1);
        final String looseToucheTokenForAccount2 = getLooseToucheToken(GOOGLE_ACCOUNT_2);

        // Test data.
        // Account 1 / contact 1.
        getMockMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForAccount1)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());
        // Account 1 / contact 2.
        getMockMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForAccount1)
                .content(getMapper().writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isCreated());
        // Account 1 / contact 3.
        getMockMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForAccount1)
                .content(getMapper().writeValueAsString(CONTACT_3.toDTO())))
                .andExpect(status().isCreated());
        // Account 2 / Contact 1.
        getMockMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForAccount2)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());
        // Account 2 / Contact 2.
        getMockMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForAccount2)
                .content(getMapper().writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isCreated());
        assertEquals(2, accountsCount());
        assertEquals(5, contactsCount());

        // Delete Account 1 / Contact 1.
        getMockMvc().perform(delete(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForAccount1))
                .andExpect(status().isOk());
        // Delete Account 1 / Contact 1 again.
        getMockMvc().perform(delete(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForAccount1))
                .andExpect(status().isNotFound());
        assertEquals(2, accountsCount());
        assertEquals(4, contactsCount());
    }

}
