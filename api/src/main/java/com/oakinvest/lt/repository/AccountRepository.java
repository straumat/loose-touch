package com.oakinvest.lt.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.oakinvest.lt.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Account repository.
 */
@Component
public class AccountRepository {

    /**
     * DynamoDB mapper.
     */
    @Autowired
    private DynamoDBMapper mapper;

    /**
     * Save a account in database.
     *
     * @param account account
     */
    public final void save(final Account account) {
        mapper.save(account);
    }

    /**
     * Returns a account by its key.
     *
     * @param id account id
     * @return account
     */
    public final Optional<Account> getAccount(final String id) {
        return Optional.ofNullable(mapper.load(Account.class, id));
    }

    /**
     * Returns a account by its google username.
     *
     * @param googleUsername google username
     * @return account
     */
    public final Optional<Account> findAccountByGoogleUsername(final String googleUsername) {

        // Set parameters.
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":googleUsername", new AttributeValue().withS(googleUsername));

        // Define the query.
        DynamoDBQueryExpression<Account> queryExpression = new DynamoDBQueryExpression<Account>()
                .withConsistentRead(false)
                .withIndexName("INDEX_GOOGLE_USERNAME")
                .withKeyConditionExpression("GOOGLE_USERNAME = :googleUsername")
                .withExpressionAttributeValues(eav);

        // Run the query.
        List<Account> account = mapper.query(Account.class, queryExpression);

        // Returns the account if exists.
        if (account.size() == 1) {
            return Optional.ofNullable(account.get(0));
        } else {
            return Optional.empty();
        }

    }

    /**
     * Delete a account.
     *
     * @param accountId account id
     */
    public final void delete(final String accountId) {
        getAccount(accountId).ifPresent(value -> mapper.delete(value));
    }

}
