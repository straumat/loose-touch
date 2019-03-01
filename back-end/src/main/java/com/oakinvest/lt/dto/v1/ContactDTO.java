package com.oakinvest.lt.dto.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Contact DTO.
 */
@SuppressWarnings({"magicnumber", "unused"})
@JsonInclude(NON_NULL)
public class ContactDTO {

    /**
     * Email.
     */
    @ApiModelProperty(value = "Email",
            example = "pascal.henri@gmail.com",
            required = true,
            position = 1)
    private String email;

    /**
     * First name.
     */
    @ApiModelProperty(value = "First name",
            example = "Pascal",
            position = 2)
    private String firstName;

    /**
     * Last name.
     */
    @ApiModelProperty(value = "Last name",
            example = "Henri",
            position = 3)
    private String lastName;

    /**
     * Notes.
     */
    @ApiModelProperty(value = "Last name",
            example = "I met him at a Java Conference",
            position = 4)
    private String notes;

    /**
     * Contact recurrence type.
     */
    @ApiModelProperty(value = "Type of recurrence (DAY, MONTH, YEAR)",
            example = "DAY",
            required = true,
            position = 5)
    private String contactRecurrenceType;

    /**
     * Contact recurrence value.
     */
    @ApiModelProperty(value = "Value of recurrence",
            example = "2",
            required = true,
            position = 6)
    private Integer contactRecurrenceValue;

    /**
     * Contact due date.
     */
    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @ApiModelProperty(value = "When the contact is due (dd/MM/yyyy)",
            example = "21/12/2019",
            required = true,
            position = 7)
    private Calendar contactDueDate;

    /**
     * Constructor.
     */
    public ContactDTO() {
    }

    /**
     * Constructor.
     *
     * @param newEmail                  email
     * @param newFirstName              first name
     * @param newLastName               last name
     * @param newNotes                  notes
     * @param newContactRecurrenceType  recurrence type
     * @param newContactRecurrenceValue recurrence value
     */
    public ContactDTO(final String newEmail,
                      final String newFirstName,
                      final String newLastName,
                      final String newNotes,
                      final String newContactRecurrenceType,
                      final int newContactRecurrenceValue) {
        this.email = newEmail;
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.notes = newNotes;
        this.contactRecurrenceType = newContactRecurrenceType;
        this.contactRecurrenceValue = newContactRecurrenceValue;
    }

    /**
     * Get email.
     *
     * @return email
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Set email.
     *
     * @param newEmail the email to set
     */
    public final void setEmail(final String newEmail) {
        email = newEmail;
    }

    /**
     * Get firstName.
     *
     * @return firstName
     */
    public final String getFirstName() {
        return firstName;
    }

    /**
     * Set firstName.
     *
     * @param newFirstName the firstName to set
     */
    public final void setFirstName(final String newFirstName) {
        firstName = newFirstName;
    }

    /**
     * Get lastName.
     *
     * @return lastName
     */
    public final String getLastName() {
        return lastName;
    }

    /**
     * Set lastName.
     *
     * @param newLastName the lastName to set
     */
    public final void setLastName(final String newLastName) {
        lastName = newLastName;
    }

    /**
     * Get notes.
     *
     * @return notes
     */
    public final String getNotes() {
        return notes;
    }

    /**
     * Set notes.
     *
     * @param newNotes the notes to set
     */
    public final void setNotes(final String newNotes) {
        notes = newNotes;
    }

    /**
     * Get contactRecurrenceType.
     *
     * @return contactRecurrenceType
     */
    public final String getContactRecurrenceType() {
        return contactRecurrenceType;
    }

    /**
     * Set contactRecurrenceType.
     *
     * @param newContactRecurrenceType the contactRecurrenceType to set
     */
    public final void setContactRecurrenceType(final String newContactRecurrenceType) {
        contactRecurrenceType = newContactRecurrenceType;
    }

    /**
     * Get contactRecurrenceValue.
     *
     * @return contactRecurrenceValue
     */
    public final Integer getContactRecurrenceValue() {
        return contactRecurrenceValue;
    }

    /**
     * Set contactRecurrenceValue.
     *
     * @param newContactRecurrenceValue the contactRecurrenceValue to set
     */
    public final void setContactRecurrenceValue(final Integer newContactRecurrenceValue) {
        contactRecurrenceValue = newContactRecurrenceValue;
    }

    /**
     * Get contactDueDate.
     *
     * @return contactDueDate
     */
    public final Calendar getContactDueDate() {
        return contactDueDate;
    }

    /**
     * Set contactDueDate.
     *
     * @param newContactDueDate the contactDueDate to set
     */
    public final void setContactDueDate(final Calendar newContactDueDate) {
        contactDueDate = newContactDueDate;
    }

}
