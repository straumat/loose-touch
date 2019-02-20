package com.oakinvest.lt.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.oakinvest.lt.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * User repository.
 */
@Component
public class UserRepository {

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
    public void init() {
        mapper = new DynamoDBMapper(dynamoDB);
    }

    /**
     * Save a user in database.
     *
     * @param user user
     */
    public final void save(final User user) {
        mapper.save(user);
    }

    /**
     * Returns a user by its key.
     *
     * @param id user id
     * @return user
     */
    public final Optional<User> getUser(final String id) {
        return Optional.ofNullable(mapper.load(User.class, id));
    }

    /**
     * Returns a user by its google username.
     *
     * @param googleUsername google username
     * @return user
     */
    public final Optional<User> findUserByGoogleUsername(final String googleUsername) {

        // Set parameters
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":googleUsername", new AttributeValue().withS(googleUsername));

        // Define the request
        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withConsistentRead(false)
                .withIndexName("INDEX_GOOGLE_USERNAME")
                .withKeyConditionExpression("GOOGLE_USERNAME = :googleUsername")
                .withExpressionAttributeValues(eav);

        List<User> users = mapper.query(User.class, queryExpression);

        // Returns the user if exists.
        if (users.size() == 1) {
            return Optional.of(users.get(0));
        } else {
            return Optional.empty();
        }

    }

    /**
     * Returns the number of users.
     * @return number of users.
     */
    public final long count() {
        ScanRequest scanRequest = new ScanRequest().withTableName("USERS");
        ScanResult result = dynamoDB.scan(scanRequest);
        return result.getCount();
    }

}
