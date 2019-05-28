package com.oakinvest.lt.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Contact.
 */
@SuppressWarnings("unused")
@DynamoDBTable(tableName = "CONTACTS")
public class Contact {

    /**
     * Separator for search content field.
     */
    private static final String SEARCH_SEPARATOR = " ";

    /**
     * Account id.
     */
    @DynamoDBHashKey(attributeName = "ACCOUNT_ID")
    private String accountId;

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
     * Search content.
     */
    @DynamoDBAttribute(attributeName = "SEARCH_CONTENT")
    private String searchContent;

    /**
     * Constructor.
     */
    public Contact() {

    }

    /**
     * Constructor.
     *
     * @param newAccountId              account id
     * @param newEmail                  email
     * @param newFirstName              first name
     * @param newLastName               last name
     * @param newNotes                  notes
     * @param newContactRecurrenceType  recurrence type
     * @param newContactRecurrenceValue recurrence value
     * @param newContactDueDate         contact due date
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public Contact(final String newAccountId,
                   final String newEmail,
                   final String newFirstName,
                   final String newLastName,
                   final String newNotes,
                   final String newContactRecurrenceType,
                   final int newContactRecurrenceValue,
                   final Calendar newContactDueDate) {
        this.accountId = newAccountId;
        this.email = newEmail;
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.notes = newNotes;
        this.contactRecurrenceType = newContactRecurrenceType;
        this.contactRecurrenceValue = newContactRecurrenceValue;
        this.contactDueDate = newContactDueDate;
    }

    /**
     * Constructor.
     *
     * @param newAccountId              account id
     * @param newEmail                  email
     * @param newFirstName              first name
     * @param newLastName               last name
     * @param newNotes                  notes
     * @param newContactRecurrenceType  recurrence type
     * @param newContactRecurrenceValue recurrence value
     * @param newContactDueDate         contact due date
     */
    @SuppressWarnings("checkstyle:parameternumber")
    public Contact(final String newAccountId,
                   final String newEmail,
                   final String newFirstName,
                   final String newLastName,
                   final String newNotes,
                   final String newContactRecurrenceType,
                   final int newContactRecurrenceValue,
                   final Date newContactDueDate) {
        this.accountId = newAccountId;
        this.email = newEmail;
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.notes = newNotes;
        this.contactRecurrenceType = newContactRecurrenceType;
        this.contactRecurrenceValue = newContactRecurrenceValue;
        this.contactDueDate = Calendar.getInstance();
        this.contactDueDate.setTime(newContactDueDate);
    }

    /**
     * Get accountId.
     *
     * @return accountId
     */
    public final String getAccountId() {
        return accountId;
    }

    /**
     * Set accountId.
     *
     * @param newAccountId the accountId to set
     */
    public final void setAccountId(final String newAccountId) {
        accountId = newAccountId;
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

    /**
     * Get searchContent.
     *
     * @return searchContent
     */
    public final String getSearchContent() {
        return searchContent;
    }

    /**
     * Set searchContent.
     *
     * @param newSearchContent the searchContent to set
     */
    public final void setSearchContent(final String newSearchContent) {
        searchContent = newSearchContent;
    }

    /**
     * Calculate the search field content.
     */
    public final void calculateSearchContent() {
        setSearchContent(StringUtils.stripAccents(getAccountId()
                + SEARCH_SEPARATOR + email
                + SEARCH_SEPARATOR + firstName
                + SEARCH_SEPARATOR + lastName
                + SEARCH_SEPARATOR + notes));
    }

}
