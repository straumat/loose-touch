package com.oakinvest.lt.test.api.v1.contact;

import com.oakinvest.lt.domain.Contact;
import com.oakinvest.lt.dto.v1.ContactDTO;
import com.oakinvest.lt.test.util.api.APITest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

import static com.oakinvest.lt.dto.v1.ContactDTO.RECURRENCE_TYPE_DAY;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_1;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_2;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_3;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_4;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_5;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_6;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_1;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_2;
import static com.oakinvest.lt.util.error.LooseTouchErrorCode.contact_recurrence_type_invalid;
import static com.oakinvest.lt.util.error.LooseTouchErrorCode.contact_recurrence_type_required;
import static com.oakinvest.lt.util.error.LooseTouchErrorCode.contact_recurrence_value_invalid;
import static com.oakinvest.lt.util.error.LooseTouchErrorCode.contact_recurrence_value_required;
import static com.oakinvest.lt.util.error.LooseTouchErrorCode.email_invalid;
import static com.oakinvest.lt.util.error.LooseTouchErrorCode.email_required;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Create contact test.
 */
public class CreateTest extends APITest {

    @Override
    public void authenticationTest() throws Exception {
        // No token provided.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Loose touch token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy bearer.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid loose touch token : invalidToken"))
                .andExpect(jsonPath("errors", hasSize(0)));
    }

    @Override
    public void validDataTest() throws Exception {
        // Configuration.
        final String looseToucheTokenForUser1 = getLooseToucheToken(GOOGLE_USER_1);

        // Trying to createContact a contact without contact data.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Contact data missing"))
                .andExpect(jsonPath("errors", hasSize(0)));
        assertEquals(getContactRepository().count(), 0);

        // Creating a ContactDTO.
        ContactDTO contact = new ContactDTO();

        // Trying to createContact a contact with empty contact data.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(contact)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Errors in the contact data"))
                .andExpect(jsonPath("errors", hasSize(3)))
                // First error.
                .andExpect(jsonPath("errors[0].code").value(email_required.toString()))
                .andExpect(jsonPath("errors[0].message").value("Email is required"))
                // Second error.
                .andExpect(jsonPath("errors[1].code").value(contact_recurrence_type_required.toString()))
                .andExpect(jsonPath("errors[1].message").value("Contact recurrence type is required"))
                // Third error.
                .andExpect(jsonPath("errors[2].code").value(contact_recurrence_value_required.toString()))
                .andExpect(jsonPath("errors[2].message").value("Contact recurrence value is required"));
        assertEquals(getContactRepository().count(), 0);

        // Trying to createContact a contact with invalid data
        contact.setEmail("invalid");
        contact.setContactRecurrenceType("contact");
        contact.setContactRecurrenceValue(-1);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(contact)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Errors in the contact data"))
                .andExpect(jsonPath("errors", hasSize(3)))
                // First error.
                .andExpect(jsonPath("errors[0].code").value(email_invalid.toString()))
                .andExpect(jsonPath("errors[0].message").value("Email is invalid"))
                // Second error.
                .andExpect(jsonPath("errors[1].code").value(contact_recurrence_type_invalid.toString()))
                .andExpect(jsonPath("errors[1].message").value("Contact recurrence type is invalid (valid values are DAY, MONTH, YEAR)"))
                // Third error.
                .andExpect(jsonPath("errors[2].code").value(contact_recurrence_value_invalid.toString()))
                .andExpect(jsonPath("errors[2].message").value("Contact recurrence value is invalid (must be between 1 and 1000)"));
        assertEquals(getContactRepository().count(), 0);

        // Trying to createContact a contact with invalid data
        contact.setEmail("test@test.fr");
        contact.setContactRecurrenceType("contact");
        contact.setContactRecurrenceValue(-1);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(contact)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Errors in the contact data"))
                .andExpect(jsonPath("errors", hasSize(2)))
                // First error.
                .andExpect(jsonPath("errors[0].code").value(contact_recurrence_type_invalid.toString()))
                .andExpect(jsonPath("errors[0].message").value("Contact recurrence type is invalid (valid values are DAY, MONTH, YEAR)"))
                // Second error.
                .andExpect(jsonPath("errors[1].code").value(contact_recurrence_value_invalid.toString()))
                .andExpect(jsonPath("errors[1].message").value("Contact recurrence value is invalid (must be between 1 and 1000)"));
        assertEquals(getContactRepository().count(), 0);

        // Trying to createContact a contact with invalid data
        contact.setEmail("test@test.fr");
        contact.setContactRecurrenceType(RECURRENCE_TYPE_DAY);
        contact.setContactRecurrenceValue(1001);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(contact)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Errors in the contact data"))
                .andExpect(jsonPath("errors", hasSize(1)))
                // First error.
                .andExpect(jsonPath("errors[0].code").value(contact_recurrence_value_invalid.toString()))
                .andExpect(jsonPath("errors[0].message").value("Contact recurrence value is invalid (must be between 1 and 1000)"));
        assertEquals(getContactRepository().count(), 0);

        // Trying to createContact a contact with valid data
        contact.setEmail("test@test.fr");
        contact.setContactRecurrenceType(RECURRENCE_TYPE_DAY);
        contact.setContactRecurrenceValue(10);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(contact)))
                .andExpect(status().isCreated());
        assertEquals(getContactRepository().count(), 1);

        // Creates contact number 1 for user 1 (date set).
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());
        assertEquals(getContactRepository().count(), 2);

        // Creates contact number 4 for user 1 (date not set).
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_4.toDTO())))
                .andExpect(status().isCreated());
        assertEquals(getContactRepository().count(), 3);

        // Try to createContact a duplicate contact.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Contact already exists"));
        assertEquals(getContactRepository().count(), 3);
    }

    @Override
    public void businessLogicTest() throws Exception {
        // User creation.
        final String looseToucheTokenForUser1 = getLooseToucheToken(GOOGLE_USER_1);
        final String user1Id = getUserRepository().findUserByGoogleUsername(GOOGLE_USER_1.getEmail()).get().getId();
        final String looseToucheTokenForUser2 = getLooseToucheToken(GOOGLE_USER_2);
        final String user2Id = getUserRepository().findUserByGoogleUsername(GOOGLE_USER_2.getEmail()).get().getId();
        assertEquals(getContactRepository().count(), 0);

        // Creates contact 1 for user 1.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());
        assertEquals(getContactRepository().count(), 1);

        // Test the contact 1 created in database.
        Optional<Contact> contactCreated = getContactRepository().findContactByEmail(user1Id, CONTACT_1.getEmail());
        assertTrue("Contact 1 not found", contactCreated.isPresent());
        assertEquals("Contact 1 email incorrect", CONTACT_1.getEmail(), contactCreated.get().getEmail());
        assertEquals("Contact 1 first name is incorrect", CONTACT_1.getFirstName(), contactCreated.get().getFirstName());
        assertEquals("Contact 1 second name is incorrect", CONTACT_1.getLastName(), contactCreated.get().getLastName());
        assertEquals("Contact 1 notes is incorrect", CONTACT_1.getNotes(), contactCreated.get().getNotes());
        assertEquals("Contact 1 recurrence type is incorrect", CONTACT_1.getContactRecurrenceType(), contactCreated.get().getContactRecurrenceType());
        assertEquals("Contact 1 recurrence value is incorrect", CONTACT_1.getContactRecurrenceValue(), contactCreated.get().getContactRecurrenceValue());
        assertEquals("Contact 1 contact due date is incorrect", "31/12/2019", getFormattedContactDueDate(contactCreated.get()));

        // Test that no other contact has been created.
        Optional<Contact> contactNotCreated = getContactRepository().findContactByEmail(user1Id, CONTACT_2.getEmail());
        assertFalse("Contact 2 found", contactNotCreated.isPresent());
        // Creates contact number 2 for user 1.
        assertEquals(getContactRepository().count(), 1);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(CONTACT_2.getEmail()))
                .andExpect(jsonPath("firstName").value(CONTACT_2.getFirstName()))
                .andExpect(jsonPath("lastName").value(CONTACT_2.getLastName()))
                .andExpect(jsonPath("notes").value(CONTACT_2.getNotes()))
                .andExpect(jsonPath("contactRecurrenceType").value(CONTACT_2.getContactRecurrenceType()))
                .andExpect(jsonPath("contactRecurrenceValue").value(CONTACT_2.getContactRecurrenceValue()))
                .andExpect(jsonPath("contactRecurrenceValue").value(CONTACT_2.getContactRecurrenceValue()))
                .andExpect(jsonPath("contactDueDate").value("16/11/2019"));
        assertEquals(getContactRepository().count(), 2);

        // Creating contacts for user1 & user 2 and check next due contact calculation algorithm.
        // User 1 / contact 1.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isBadRequest());
        // User 1 / contact 2.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isBadRequest());
        // User 1 / contact 3.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_3.toDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("contactDueDate").value("01/09/2019"));
        // User 1 / contact 4.
        Calendar date = Calendar.getInstance();
        date.add(DATE, 4);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_4.toDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("contactDueDate").value(new SimpleDateFormat(DATE_FORMAT).format(date.getTime())));
        // User 1 / Contact 5.
        date = Calendar.getInstance();
        date.add(MONTH, 5);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_5.toDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("contactDueDate").value(new SimpleDateFormat(DATE_FORMAT).format(date.getTime())));
        // User 1 / Contact 6.
        date = Calendar.getInstance();
        date.add(YEAR, 6);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_6.toDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("contactDueDate").value(new SimpleDateFormat(DATE_FORMAT).format(date.getTime())));
        // User 2 / Contact 1.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());
        // User 2 / Contact 1 (error duplicate).
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isBadRequest());
        // User 2 / Contact 2.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(getMapper().writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isCreated());
        assertEquals(getContactRepository().count(), 8);

        // TODO Test creating two times the contact
    }

    /**
     * Returns formatted date of a contact.
     *
     * @param c contact
     * @return formatted date
     */
    private String getFormattedContactDueDate(Contact c) {
        return new SimpleDateFormat(DATE_FORMAT).format(c.getContactDueDate().getTime());
    }

}
