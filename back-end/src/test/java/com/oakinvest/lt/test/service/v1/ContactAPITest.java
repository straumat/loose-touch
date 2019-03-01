package com.oakinvest.lt.test.service.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oakinvest.lt.domain.Contact;
import com.oakinvest.lt.dto.v1.ContactDTO;
import com.oakinvest.lt.test.util.junit.JUnitHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

import static com.oakinvest.lt.configuration.Application.LOCAL_DYNAMODB_ENVIRONMENT;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_1;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_2;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_3;
import static com.oakinvest.lt.test.util.data.TestContacts.CONTACT_4;
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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Contact API test.
 */
@ActiveProfiles(LOCAL_DYNAMODB_ENVIRONMENT)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ContactAPITest extends JUnitHelper {

    /**
     * Google login URL.
     */
    protected static final String CONTACT_URL = "/v1/contacts";

    /**
     * Create test.
     *
     * @throws Exception exception
     */
    @Test
    public void createTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Configuration
        final String looseToucheTokenForUser1 = getLooseToucheToken(GOOGLE_USER_1);
        final String user1Id = getUserRepository().findUserByGoogleUsername(GOOGLE_USER_1.getEmail()).get().getId();
        final String looseToucheTokenForUser2 = getLooseToucheToken(GOOGLE_USER_2);
        final String user2Id = getUserRepository().findUserByGoogleUsername(GOOGLE_USER_2.getEmail()).get().getId();

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

        // Trying to create a contact without contact data.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Contact data missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Creating a ContactDTO.
        ContactDTO contact = new ContactDTO();

        // Trying to create a contact with empty contact data.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(mapper.writeValueAsString(contact)))
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

        // Trying to create a contact with invalid data
        contact.setEmail("invalid");
        contact.setContactRecurrenceType("contact");
        contact.setContactRecurrenceValue(-1);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(mapper.writeValueAsString(contact)))
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

        // Creates contact number 1 for user 1.
        assertEquals(getContactRepository().count(), 0);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(mapper.writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(CONTACT_1.getEmail()))
                .andExpect(jsonPath("firstName").value(CONTACT_1.getFirstName()))
                .andExpect(jsonPath("lastName").value(CONTACT_1.getLastName()))
                .andExpect(jsonPath("notes").value(CONTACT_1.getNotes()))
                .andExpect(jsonPath("contactRecurrenceType").value(CONTACT_1.getContactRecurrenceType()))
                .andExpect(jsonPath("contactRecurrenceValue").value(CONTACT_1.getContactRecurrenceValue()))
                .andExpect(jsonPath("contactRecurrenceValue").value(CONTACT_1.getContactRecurrenceValue()))
                .andExpect(jsonPath("contactDueDate").value("01/01/2020"));
        assertEquals(getContactRepository().count(), 1);

        // Try to create a duplicate contact.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(mapper.writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Contact already exists"));
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
        assertEquals("Contact 1 contact due date is incorrect", "01/01/2020", new SimpleDateFormat("dd/MM/YYYY").format(contactCreated.get().getContactDueDate().getTime()));

        // Test that no other contact has been created.
        Optional<Contact> contactNotCreated = getContactRepository().findContactByEmail(user1Id, CONTACT_2.getEmail());
        assertFalse("Contact 2 found", contactNotCreated.isPresent());
        // Creates contact number 2 for user 1.
        assertEquals(getContactRepository().count(), 1);
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(mapper.writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(CONTACT_2.getEmail()))
                .andExpect(jsonPath("firstName").value(CONTACT_2.getFirstName()))
                .andExpect(jsonPath("lastName").value(CONTACT_2.getLastName()))
                .andExpect(jsonPath("notes").value(CONTACT_2.getNotes()))
                .andExpect(jsonPath("contactRecurrenceType").value(CONTACT_2.getContactRecurrenceType()))
                .andExpect(jsonPath("contactRecurrenceValue").value(CONTACT_2.getContactRecurrenceValue()))
                .andExpect(jsonPath("contactRecurrenceValue").value(CONTACT_2.getContactRecurrenceValue()))
                .andExpect(jsonPath("contactDueDate").value("29/02/2020"));
        assertEquals(getContactRepository().count(), 2);

        // Creating contacts for user1 & user 2.
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(mapper.writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isBadRequest());
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(mapper.writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isBadRequest());
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(mapper.writeValueAsString(CONTACT_3.toDTO())))
                .andExpect(status().isCreated());
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser1)
                .content(mapper.writeValueAsString(CONTACT_4.toDTO())))
                .andExpect(status().isCreated());
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(mapper.writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isCreated());
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(mapper.writeValueAsString(CONTACT_1.toDTO())))
                .andExpect(status().isBadRequest());
        getMvc().perform(post(CONTACT_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + looseToucheTokenForUser2)
                .content(mapper.writeValueAsString(CONTACT_2.toDTO())))
                .andExpect(status().isCreated());
        assertEquals(getContactRepository().count(), 6);

    }

}