package com.oakinvest.lt.service.v1;

import com.oakinvest.lt.authentication.loosetouch.AuthenticatedUser;
import com.oakinvest.lt.domain.Contact;
import com.oakinvest.lt.dto.v1.ContactDTO;
import com.oakinvest.lt.repository.ContactRepository;
import com.oakinvest.lt.util.error.LooseTouchErrorCode;
import com.oakinvest.lt.util.error.LooseTouchErrorDetail;
import com.oakinvest.lt.util.error.LooseTouchException;
import com.oakinvest.lt.util.mapper.LooseTouchMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.oakinvest.lt.domain.ContactRecurrenceType.DAY;
import static com.oakinvest.lt.domain.ContactRecurrenceType.MONTH;
import static com.oakinvest.lt.domain.ContactRecurrenceType.YEAR;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.resource_not_found;

/**
 * Contact controller.
 */
@RestController
public class ContactController implements ContactAPI {

    /**
     * Contact recurrence minimum value.
     */
    private static final int CONTACT_RECURRENCE_MINIMUM_VALUE = 1;

    /**
     * Contact recurrence maximum value.
     */
    private static final int CONTACT_RECURRENCE_MAXIMUM_VALUE = 1000;

    /**
     * Contact repository.
     */
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public final ContactDTO create(final AuthenticatedUser authenticatedUser, final ContactDTO contact) {

        // =============================================================================================================
        // Managing errors.

        // A contact must be set.
        if (contact == null) {
            throw new LooseTouchException(invalid_request_error, "Contact data missing");
        }

        // Test contact data.
        List<LooseTouchErrorDetail> errors = getContactDataErrors(contact);
        if (errors.size() > 0) {
            throw new LooseTouchException(invalid_request_error, "Errors in the contact data", errors);
        }

        // Test if the contact already exists.
        if (contactRepository.findContactByEmail(authenticatedUser.getUserId(), contact.getEmail()).isPresent()) {
            throw new LooseTouchException(invalid_request_error, "Contact already exists");
        }

        // =============================================================================================================
        // Creates the contact and returns it.

        // If date is set, set a new date.
        Calendar dueDate;
        if (contact.getContactDueDate() != null) {
            dueDate = calculateNextContactDate(contact.getContactDueDate(), contact.getContactRecurrenceType(), contact.getContactRecurrenceValue());
        } else {
            dueDate = calculateNextContactDate(Calendar.getInstance(), contact.getContactRecurrenceType(), contact.getContactRecurrenceValue());
        }

        // Create contact.
        Contact contactToCreate = new Contact(authenticatedUser.getUserId(),
                contact.getEmail(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getNotes(),
                contact.getContactRecurrenceType(),
                contact.getContactRecurrenceValue(),
                dueDate);
        contactRepository.save(contactToCreate);
        return LooseTouchMapper.INSTANCE.contactToContactDTO(contactToCreate);
    }

    @Override
    public final ContactDTO getContact(final AuthenticatedUser authenticatedUser, final String email) {
        Optional<Contact> contact = contactRepository.findContactByEmail(authenticatedUser.getUserId(), email);
        if (contact.isPresent()) {
            return LooseTouchMapper.INSTANCE.contactToContactDTO(contact.get());
        } else {
            throw new LooseTouchException(resource_not_found, "Contact not found");
        }
    }

    @Override
    public final ContactDTO update(final AuthenticatedUser authenticatedUser, final String email, final ContactDTO contact) {
        return null;
    }

    /**
     * Calculate next contact due date.
     *
     * @param startDate              date to start for calculation.
     * @param contactRecurrenceType  contact recurrence type
     * @param contactRecurrenceValue contact recurrence value
     * @return next due date
     */
    private Calendar calculateNextContactDate(final Calendar startDate, final String contactRecurrenceType, final int contactRecurrenceValue) {
        Calendar dueDate = startDate;
        switch (contactRecurrenceType) {
            case "DAY":
                dueDate.add(Calendar.DATE, contactRecurrenceValue);
                break;
            case "MONTH":
                dueDate.add(Calendar.MONTH, contactRecurrenceValue);
                break;
            case "YEAR":
                dueDate.add(Calendar.YEAR, contactRecurrenceValue);
                break;
            default:
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
        return DateUtils.truncate(dueDate, Calendar.DATE);
    }

    /**
     * Check contactDTO values and returns errors.
     *
     * @param contact contact DTO
     * @return list of errors.
     */
    private List<LooseTouchErrorDetail> getContactDataErrors(final ContactDTO contact) {
        // Errors.
        final List<LooseTouchErrorDetail> errors = new LinkedList<>();

        // Email
        if (contact.getEmail() == null) {
            errors.add(new LooseTouchErrorDetail(LooseTouchErrorCode.email_required, "Email is required"));
        } else {
            if (!EmailValidator.getInstance().isValid(contact.getEmail())) {
                errors.add(new LooseTouchErrorDetail(LooseTouchErrorCode.email_invalid, "Email is invalid"));
            }
        }

        // Check contact recurrence type is set.
        if (contact.getContactRecurrenceType() == null) {
            errors.add(new LooseTouchErrorDetail(LooseTouchErrorCode.contact_recurrence_type_required, "Contact recurrence type is required"));
        } else {
            // Check contact recurrence value.
            if (!contact.getContactRecurrenceType().equals(DAY.getValue())
                    && !contact.getContactRecurrenceType().equals(MONTH.getValue())
                    && !contact.getContactRecurrenceType().equals(YEAR.getValue())
            ) {
                errors.add(new LooseTouchErrorDetail(LooseTouchErrorCode.contact_recurrence_type_invalid, "Contact recurrence type is invalid (valid values are DAY, MONTH, YEAR)"));
            }
        }

        // Check contact recurrence value is set.
        if (contact.getContactRecurrenceValue() == null) {
            errors.add(new LooseTouchErrorDetail(LooseTouchErrorCode.contact_recurrence_value_required, "Contact recurrence value is required"));
        } else {
            if (contact.getContactRecurrenceValue() < CONTACT_RECURRENCE_MINIMUM_VALUE || contact.getContactRecurrenceValue() > CONTACT_RECURRENCE_MAXIMUM_VALUE) {
                errors.add(new LooseTouchErrorDetail(LooseTouchErrorCode.contact_recurrence_value_invalid, "Contact recurrence value is invalid (must be between 1 and 1000)"));
            }
        }

        return errors;
    }

}
