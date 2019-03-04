package com.oakinvest.lt.test.api.v1.contact;

import com.oakinvest.lt.dto.v1.ContactDTO;
import com.oakinvest.lt.test.util.api.APITest;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_1;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_1;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_2;
import static com.oakinvest.lt.util.error.LooseTouchErrorCode.contact_recurrence_type_invalid;
import static com.oakinvest.lt.util.error.LooseTouchErrorCode.contact_recurrence_value_invalid;
import static com.oakinvest.lt.util.error.LooseTouchErrorCode.email_invalid;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.resource_not_found;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Update contact test.
 */
public class UpdateContactTest extends APITest {

    @Override
    public void authenticationTest() throws Exception {
        // No token provided.
        getMvc().perform(put(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Loose touch token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy bearer.
        getMvc().perform(put(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
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

        // Invalid email address
        getMvc().perform(put(CONTACT_URL + "/toto/")
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("type").value(resource_not_found.toString()))
                .andExpect(jsonPath("message").value("Contact not found"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Creates User 1 / contact 1.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());

        // No contact data.
        getMvc().perform(put(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Contact data are missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Wrong test data.
        ContactDTO contact = new ContactDTO();

        // Updating a not existing contact.
        getMvc().perform(put(CONTACT_URL + "/toto@toto.fr/")
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .contentType(APPLICATION_JSON_UTF8)
                .content(getMapper().writeValueAsString(contact)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("type").value(resource_not_found.toString()))
                .andExpect(jsonPath("message").value("Contact not found"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Trying to createContact a contact with invalid data
        contact.setEmail("invalid");
        contact.setContactRecurrenceType("contact");
        contact.setContactRecurrenceValue(-1);
        getMvc().perform(put(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
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


        // Trying to createContact a contact with invalid data
        contact.setEmail("test@test.fr");
        contact.setContactRecurrenceType("contact");
        contact.setContactRecurrenceValue(-1);
        getMvc().perform(put(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
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

        // Trying to createContact a contact with invalid data
        contact.setEmail("test@test.fr");
        contact.setContactRecurrenceType("DAY");
        contact.setContactRecurrenceValue(1001);
        getMvc().perform(put(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
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

        // Trying to createContact a contact with valid data
        contact.setEmail("test@test.fr");
        contact.setContactRecurrenceType("DAY");
        contact.setContactRecurrenceValue(10);
        getMvc().perform(put(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(contact)))
                .andExpect(status().isOk());
        assertEquals(getContactRepository().count(), 1);
    }

    @Override
    public void businessLogicTest() throws Exception {
        // Configuration.
        final String looseToucheTokenForUser1 = getLooseToucheToken(GOOGLE_USER_1);
        final String looseToucheTokenForUser2 = getLooseToucheToken(GOOGLE_USER_2);

        // Creates User 1 / contact 1.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());
        // Creates User 2 / contact 1.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(getMapper().writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());

        // Updates User 1 / Contact1 without changing the date.
        ContactDTO contact = new ContactDTO();
        contact.setEmail(CONTACT_1.getEmail());
        contact.setFirstName("first name test 1 (update user1");
        contact.setLastName("last name test 1 (update user1");
        contact.setNotes("notes 1 (update user1)");
        contact.setContactRecurrenceType("YEAR");
        contact.setContactRecurrenceValue(9);
        getMvc().perform(put(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(contact)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(CONTACT_1.getEmail()))
                .andExpect(jsonPath("firstName").value("first name test 1 (update user1"))
                .andExpect(jsonPath("lastName").value("last name test 1 (update user1"))
                .andExpect(jsonPath("notes").value("notes 1 (update user1)"))
                .andExpect(jsonPath("contactRecurrenceType").value("YEAR"))
                .andExpect(jsonPath("contactRecurrenceValue").value(9))
                .andExpect(jsonPath("contactDueDate").value("31/12/2019"));
        // Updates the contact due date.
        contact.setContactDueDate(new GregorianCalendar(1978, Calendar.AUGUST, 8, 0, 0, 1));
        getMvc().perform(put(CONTACT_URL + "/" + CONTACT_1.getEmail() + "/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(getMapper().writeValueAsString(contact)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(CONTACT_1.getEmail()))
                .andExpect(jsonPath("firstName").value("first name test 1 (update user1"))
                .andExpect(jsonPath("lastName").value("last name test 1 (update user1"))
                .andExpect(jsonPath("notes").value("notes 1 (update user1)"))
                .andExpect(jsonPath("contactRecurrenceType").value("YEAR"))
                .andExpect(jsonPath("contactRecurrenceValue").value(9))
                .andExpect(jsonPath("contactDueDate").value("08/08/1978"));

        // Checking that contact 1 of user 2 did not change.
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
        assertEquals(getContactRepository().count(), 2);
    }

}
