package com.oakinvest.lt.test.api.v1.account;

import com.oakinvest.lt.test.util.api.APITest;
import org.junit.Ignore;

import java.util.Calendar;

import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_1;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_2;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_3;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_4;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_5;
import static com.oakinvest.lt.test.util.data.TestAccounts.GOOGLE_ACCOUNT_1;
import static com.oakinvest.lt.test.util.data.TestAccounts.GOOGLE_ACCOUNT_2;
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
public class DeleteAccountTest extends APITest {

    @Override
    public void authenticationTest() throws Exception {
        // No token provided.
        getMockMvc().perform(delete(DELETE_ACCOUNT_URL)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Loose touch token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy bearer.
        getMockMvc().perform(delete(DELETE_ACCOUNT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid loose touch token : invalidToken"))
                .andExpect(jsonPath("errors", hasSize(0)));
    }

    @Ignore("Impossible that account id is not set")
    @Override
    public void validDataTest() {

    }

    @Override
    public void businessLogicTest() throws Exception {
        // Account creation.
        assertEquals(0, accountsCount());
        assertEquals(0, contactsCount());
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
        // Account 1 / contact 4.
        Calendar date = Calendar.getInstance();
        date.add(DATE, 4);
        getMockMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForAccount1)
                .content(getMapper().writeValueAsString(CONTACT_4.toDTO())))
                .andExpect(status().isCreated());
        // Account 1 / Contact 5.
        date = Calendar.getInstance();
        date.add(MONTH, 5);
        getMockMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForAccount1)
                .content(getMapper().writeValueAsString(CONTACT_5.toDTO())))
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
        assertEquals(7, contactsCount());

        // Delete account 1.
        getMockMvc().perform(delete(DELETE_ACCOUNT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForAccount1))
                .andExpect(status().isOk());
        assertEquals(1, accountsCount());
        assertEquals(2, contactsCount());

        // Delete account 2.
        getMockMvc().perform(delete(DELETE_ACCOUNT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForAccount2))
                .andExpect(status().isOk());
        assertEquals(0, accountsCount());
        assertEquals(0, contactsCount());
    }

}
