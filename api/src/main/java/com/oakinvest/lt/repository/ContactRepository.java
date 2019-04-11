package com.oakinvest.lt.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.oakinvest.lt.domain.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * Contact repository.
 */
@Component
public class ContactRepository {

    /**
     * DynamoDB mapper.
     */
    @Autowired
    private DynamoDBMapper mapper;

    /**
     * Save a contact in database.
     *
     * @param contact contact
     */
    public final void save(final Contact contact) {
        mapper.save(contact);
    }

    /**
     * Delete a contact in database.
     *
     * @param contact contact
     */
    public final void delete(final Contact contact) {
        mapper.delete(contact);
    }

    /**
     * Delete all contacts of account.
     *
     * @param accountId account id
     */
    public final void deleteAllContactsOfAccount(final String accountId) {
        // Account id.
        Contact c = new Contact();
        c.setAccountId(accountId);

        // Build the query.
        DynamoDBQueryExpression<Contact> queryExpression = new DynamoDBQueryExpression<Contact>()
                .withHashKeyValues(c);

        // Run the query to retrieve all contacts.
        List<Contact> contactsToDelete = mapper.query(Contact.class, queryExpression);

        // Delete them.
        mapper.batchDelete(contactsToDelete);
    }

    /**
     * Returns a contact by its email.
     *
     * @param accountId account id
     * @param email  contact email
     * @return contact
     */
    public final Optional<Contact> findContactByEmail(final String accountId, final String email) {
        // Account id.
        Contact c = new Contact();
        c.setAccountId(accountId);

        // Email.
        Condition rangeKeyCondition = new Condition();
        rangeKeyCondition.withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(email));

        // Define the query.
        DynamoDBQueryExpression<Contact> queryExpression = new DynamoDBQueryExpression<Contact>()
                .withHashKeyValues(c)
                .withRangeKeyCondition("EMAIL", rangeKeyCondition);

        // Run the query.
        List<Contact> contacts = mapper.query(Contact.class, queryExpression);

        // Returns the account if exists.
        if (contacts.size() == 1) {
            return Optional.ofNullable(contacts.get(0));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns the list of contacts to reach.
     *
     * @param accountId account id
     * @return contact to reach
     */
    public final List<Contact> getContactsToReach(final String accountId) {
        // Account id.
        Contact c = new Contact();
        c.setAccountId(accountId);

        // Set the date as today.
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Condition rangeKeyCondition = new Condition();
        rangeKeyCondition.withComparisonOperator(ComparisonOperator.LE)
                .withAttributeValueList(new AttributeValue().withS(dateFormatter.format(new Date())));

        // Define the query.
        DynamoDBQueryExpression<Contact> queryExpression = new DynamoDBQueryExpression<Contact>()
                .withHashKeyValues(c)
                .withConsistentRead(false)
                .withIndexName("INDEX_CONTACT_DUE_DATE")
                .withRangeKeyCondition("CONTACT_DUE_DATE", rangeKeyCondition);

        return mapper.query(Contact.class, queryExpression);
    }

}
