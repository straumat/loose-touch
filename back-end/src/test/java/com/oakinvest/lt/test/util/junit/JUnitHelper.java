package com.oakinvest.lt.test.util.junit;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.oakinvest.lt.domain.User;
import com.oakinvest.lt.test.authentication.googleTokenVerifierTest;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * JUnit helper.
 */
public class JUnitHelper {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(JUnitHelper.class);

    /**
     * Dynamo DB Server.
     */
    private DynamoDBProxyServer server;

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
     * Start DynamoDB.
     */
    @Before
    public void startDynamoDB() throws Exception {
        System.setProperty("sqlite4java.library.path", "native-libs");
        final String[] localArgs = {"-sharedDb", "-inMemory"};
        server = ServerRunner.createServerFromCommandLineArgs(localArgs);
        server.start();

        mapper = new DynamoDBMapper(dynamoDB);

        // Drop the tables.
/*        DeleteTableRequest deleteUserTableRequest = mapper.generateDeleteTableRequest(User.class);
        TableUtils.deleteTableIfExists(dynamoDB, deleteUserTableRequest);*/

        // Creates the tables.
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(5L, 5L);
        CreateTableRequest createUserTableRequest = mapper.generateCreateTableRequest(User.class)
                .withProvisionedThroughput(provisionedThroughput);
        createUserTableRequest.getGlobalSecondaryIndexes().forEach(v -> v.setProvisionedThroughput(provisionedThroughput));
        CreateTableResult table = dynamoDB.createTable(createUserTableRequest);

        // Waits for every table to be created.
        TableUtils.waitUntilActive(dynamoDB, createUserTableRequest.getTableName());
    }

    /**
     * Stop DynamoDB.
     *
     * @throws Exception exception
     */
    public void stopDynamoDB() throws Exception {
        server.stop();
    }

    /**
     * Get mapper.
     *
     * @return mapper
     */
    public final DynamoDBMapper getMapper() {
        return mapper;
    }
}
