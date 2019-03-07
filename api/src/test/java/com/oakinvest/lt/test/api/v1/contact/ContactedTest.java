package com.oakinvest.lt.test.api.v1.contact;

import com.oakinvest.lt.test.util.api.APITest;
import org.junit.Ignore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_1;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_2;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_3;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_4;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_5;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_6;
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
 * Contacted test.
 */
public class ContactedTest extends APITest {

    @Override
    public void authenticationTest() throws Exception {
        // No token provided.
        getMvc().perform(get(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/contacted")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Loose touch token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy bearer.
        getMvc().perform(get(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/contacted")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid loose touch token : invalidToken"))
                .andExpect(jsonPath("errors", hasSize(0)));
    }

    @Ignore("Email parameter is required")
    @Override
    public void validDataTest() throws Exception {

    }

    @Override
    public void businessLogicTest() throws Exception {
        // Configuration.
        final String looseToucheTokenForUser1 = getLooseToucheToken(GOOGLE_USER_1);
        final String looseToucheTokenForUser2 = getLooseToucheToken(GOOGLE_USER_2);

        // =============================================================================================================
        // Test data.
        // Creates User 1 / contact 1.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());
        // Creates User 1 / contact 2.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isCreated());
        // Creates User 1 / contact 3.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_3.toDTO())))
                .andExpect(status().isCreated());
        // Creates User 1 / contact 4.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_4.toDTO())))
                .andExpect(status().isCreated());
        // Creates User 1 / contact 5.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_5.toDTO())))
                .andExpect(status().isCreated());
        // Creates User 1 / contact 6.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_6.toDTO())))
                .andExpect(status().isCreated());
        // Creates User 2 / contact 1.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());
        // Creates User 2 / contact 2.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(getMapper().writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isCreated());
        // Creates User 2 / contact 3.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(getMapper().writeValueAsString(CONTACT_3.toDTO())))
                .andExpect(status().isCreated());

        // =============================================================================================================
        // Test the new date.
        String contactDueDate;

        // user 1 / contact 1 contacted.
        contactDueDate = calculateNextContactDueDate(CONTACT_1.getContactRecurrenceType(),
                CONTACT_1.getContactRecurrenceValue());
        getMvc().perform(get(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/contacted")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("contactDueDate").value(contactDueDate));
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
                .andExpect(jsonPath("contactDueDate").value(contactDueDate));

        // Check that user 2 / contact 1 did not change.
        getMvc().perform(get(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(CONTACT_1.getEmail()))
                .andExpect(jsonPath("firstName").value(CONTACT_1.getFirstName()))
                .andExpect(jsonPath("lastName").value(CONTACT_1.getLastName()))
                .andExpect(jsonPath("notes").value(CONTACT_1.getNotes()))
                .andExpect(jsonPath("contactRecurrenceType").value(CONTACT_1.getContactRecurrenceType()))
                .andExpect(jsonPath("contactRecurrenceValue").value(CONTACT_1.getContactRecurrenceValue()))
                .andExpect(jsonPath("contactRecurrenceValue").value(CONTACT_1.getContactRecurrenceValue()))
                .andExpect(jsonPath("contactDueDate").value("31/12/2019"));

        // user 1 / contact 2 contacted.
        contactDueDate = calculateNextContactDueDate(CONTACT_2.getContactRecurrenceType(),
                CONTACT_2.getContactRecurrenceValue());
        getMvc().perform(get(CONTACT_URL + "/" + CONTACT_2.getEmail() + "/contacted")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("contactDueDate").value(contactDueDate));
    }

    /**
     * Calculate next contact due date.
     *
     * @param contactRecurrenceType  contact recurrence type
     * @param contactRecurrenceValue contact recurrence value
     * @return next due date
     */
    private String calculateNextContactDueDate(final String contactRecurrenceType, final int contactRecurrenceValue) {
        return calculateNextContactDueDate(Calendar.getInstance(), contactRecurrenceType, contactRecurrenceValue);
    }

    /**
     * Calculate next contact due date.
     *
     * @param startDate              date to start for calculation.
     * @param contactRecurrenceType  contact recurrence type
     * @param contactRecurrenceValue contact recurrence value
     * @return next due date
     */
    private String calculateNextContactDueDate(final Calendar startDate, final String contactRecurrenceType, final int contactRecurrenceValue) {
        switch (contactRecurrenceType) {
            case "DAY":
                startDate.add(Calendar.DATE, contactRecurrenceValue);
                break;
            case "MONTH":
                startDate.add(Calendar.MONTH, contactRecurrenceValue);
                break;
            case "YEAR":
                startDate.add(Calendar.YEAR, contactRecurrenceValue);
                break;
            default:
                break;
        }
        return new SimpleDateFormat(DATE_FORMAT).format(startDate.getTime());
    }

}
