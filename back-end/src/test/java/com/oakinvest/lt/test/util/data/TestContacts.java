package com.oakinvest.lt.test.util.data;

import com.oakinvest.lt.domain.Contact;
import com.oakinvest.lt.dto.v1.ContactDTO;
import com.oakinvest.lt.dto.v1.UserDTO;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Test contacts.
 */
public enum TestContacts {

    /**
     * Contact 1
     */
    CONTACT_1("test1@test.com",
            "first name test 1",
            "last name test 1",
            "notes 1",
            "DAY",
            1,
            new GregorianCalendar(2019, Calendar.DECEMBER, 31, 13, 24, 56)
    ),

    /**
     * Contact 2
     */
    CONTACT_2("test2@test.com",
            "first name test 2",
            "last name test 2",
            "notes 2",
            "MONTH",
            2,
            new GregorianCalendar(2019, Calendar.DECEMBER, 31, 13, 24, 56)
    ),

    /**
     * Contact 3
     */
    CONTACT_3("test3@test.com",
            "first name test 3",
            "last name test 3",
            "notes 3",
            "YEAR",
            3,
            new GregorianCalendar(2019, Calendar.DECEMBER, 31, 13, 24, 56)
    ),

    /**
     * Contact 4
     */
    CONTACT_4("test4@test.com",
            "first name test 4",
            "last name test 4",
            "notes 4",
            "DAY",
            4,
            new GregorianCalendar(2019, Calendar.DECEMBER, 31, 13, 24, 56)
    ),

    /**
     * Contact 5
     */
    CONTACT_5("test5@test.com",
            "first name test 5",
            "last name test 5",
            "notes 5",
            "MONTH",
            5,
            new GregorianCalendar(2019, Calendar.DECEMBER, 31, 13, 24, 56)
    ),

    /**
     * Contact 6
     */
    CONTACT_6("test6@test.com",
            "first name test 6",
            "last name test 6",
            "notes 6",
            "YEAR",
            6,
            new GregorianCalendar(2019, Calendar.DECEMBER, 31, 13, 24, 56)
    );

    /**
     * Constructor.
     *
     * @param newEmail                  email
     * @param newFirstName              first name
     * @param newLastName               last name
     * @param newNotes                  notes
     * @param newContactRecurrenceType  recurrence type
     * @param newContactRecurrenceValue recurrence value
     * @param newContactDueDate         contact due date
     */
    TestContacts(final String newEmail,
                 final String newFirstName,
                 final String newLastName,
                 final String newNotes,
                 final String newContactRecurrenceType,
                 final int newContactRecurrenceValue,
                 final Calendar newContactDueDate) {
        this.email = newEmail;
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.notes = newNotes;
        this.contactRecurrenceType = newContactRecurrenceType;
        this.contactRecurrenceValue = newContactRecurrenceValue;
        this.contactDueDate = newContactDueDate;
    }

    /**
     * Email.
     */
    private String email;

    /**
     * First name.
     */
    private String firstName;

    /**
     * Last name.
     */
    private String lastName;

    /**
     * Notes.
     */
    private String notes;

    /**
     * Contact recurrence type (DAY, MONTH, YEAR).
     */
    private String contactRecurrenceType;

    /**
     * Contact recurrence value.
     */
    private int contactRecurrenceValue;

    /**
     * Contact due date.
     */
    private Calendar contactDueDate;

    /**
     * Returns a UserDTO.
     *
     * @return userDTO
     */
    public ContactDTO toDTO() {
        ContactDTO contactDTO = new ContactDTO(getEmail(),
                getFirstName(),
                getLastName(),
                getNotes(),
                getContactRecurrenceType(),
                getContactRecurrenceValue());
        contactDTO.setContactDueDate(getContactDueDate());
        return contactDTO;
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
     * Get firstName.
     *
     * @return firstName
     */
    public final String getFirstName() {
        return firstName;
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
     * Get notes.
     *
     * @return notes
     */
    public final String getNotes() {
        return notes;
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
     * Get contactRecurrenceValue.
     *
     * @return contactRecurrenceValue
     */
    public final int getContactRecurrenceValue() {
        return contactRecurrenceValue;
    }

    /**
     * Get contactDueDate.
     *
     * @return contactDueDate
     */
    public final Calendar getContactDueDate() {
        return contactDueDate;
    }

}
