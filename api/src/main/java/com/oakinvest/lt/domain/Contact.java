package com.oakinvest.lt.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Calendar;

/**
 * Contact.
 */
@SuppressWarnings("unused")
@DynamoDBTable(tableName = "CONTACTS")
public class Contact {

    /**
     * User id.
     */
    @DynamoDBHashKey(attributeName = "USER_ID")
    private String userId;

    /**
     * Email.
     */
    @DynamoDBRangeKey(attributeName = "EMAIL")
    private String email;

    /**
     * First name.
     */
    @DynamoDBAttribute(attributeName = "FIRST_NAME")
    private String firstName;

    /**
     * Last name.
     */
    @DynamoDBAttribute(attributeName = "LAST_NAME")
    private String lastName;

    /**
     * Notes.
     */
    @DynamoDBAttribute(attributeName = "NOTES")
    private String notes;

    /**
     * Contact recurrence type (DAY, MONTH, YEAR).
     */
    @DynamoDBAttribute(attributeName = "CONTACT_RECURRENCE_TYPE")
    private String contactRecurrenceType;

    /**
     * Contact recurrence value.
     */
    @DynamoDBAttribute(attributeName = "CONTACT_RECURRENCE_VALUE")
    private int contactRecurrenceValue;

    /**
     * Contact due date.
     */
    @DynamoDBIndexRangeKey(attributeName = "CONTACT_DUE_DATE", localSecondaryIndexName = "INDEX_CONTACT_DUE_DATE")
    private Calendar contactDueDate;

    /**
     * Constructor.
     */
    public Contact() {

    }

    /**
     * Constructor.
     *
     * @param newUserId                 user id
     * @param newEmail                  email
     * @param newFirstName              first name
     * @param newLastName               last name
     * @param newNotes                  notes
     * @param newContactRecurrenceType  recurrence type
     * @param newContactRecurrenceValue recurrence value
     * @param newContactDueDate         contact due date
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public Contact(final String newUserId,
                   final String newEmail,
                   final String newFirstName,
                   final String newLastName,
                   final String newNotes,
                   final String newContactRecurrenceType,
                   final int newContactRecurrenceValue,
                   final Calendar newContactDueDate) {
        this.userId = newUserId;
        this.email = newEmail;
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.notes = newNotes;
        this.contactRecurrenceType = newContactRecurrenceType;
        this.contactRecurrenceValue = newContactRecurrenceValue;
        this.contactDueDate = newContactDueDate;
    }

    /**
     * Get userId.
     *
     * @return userId
     */
    public final String getUserId() {
        return userId;
    }

    /**
     * Set userId.
     *
     * @param newUserId the userId to set
     */
    public final void setUserId(final String newUserId) {
        userId = newUserId;
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
    public final int getContactRecurrenceValue() {
        return contactRecurrenceValue;
    }

    /**
     * Set contactRecurrenceValue.
     *
     * @param newContactRecurrenceValue the contactRecurrenceValue to set
     */
    public final void setContactRecurrenceValue(final int newContactRecurrenceValue) {
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
