package com.oakinvest.lt.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.oakinvest.lt.domain.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * Contact repository.
 */
@Component
public class ContactRepository {

    /**
     * DynamoDB connection.
     */
    @Autowired
    private AmazonDynamoDB dynamoDB;

    /**
     * DynamoDB mapper.
     */
    private DynamoDBMapper mapper;

    /**
     * Constructor.
     */
    @PostConstruct
    public final void init() {
        mapper = new DynamoDBMapper(dynamoDB);
    }

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
     * Returns a contact by its email.
     *
     * @param userId user id
     * @param email  contact email
     * @return contact
     */
    public final Optional<Contact> findContactByEmail(final String userId, final String email) {

        // User id.
        Contact c = new Contact();
        c.setUserId(userId);

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

        // Returns the user if exists.
        if (contacts.size() == 1) {
            return Optional.ofNullable(contacts.get(0));
        } else {
            return Optional.empty();
        }

    }

    /**
     * Returns the number of users.
     *
     * @return number of users.
     */
    public final long count() {
        ScanRequest scanRequest = new ScanRequest().withTableName("CONTACTS");
        ScanResult result = dynamoDB.scan(scanRequest);
        return result.getCount();
    }

}
