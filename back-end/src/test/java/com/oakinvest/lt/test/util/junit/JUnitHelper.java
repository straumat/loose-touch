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
import com.oakinvest.lt.test.util.authentication.GoogleTokenRetriever;
import com.oakinvest.lt.test.util.authentication.GoogleTokenRetrieverUser;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.oakinvest.lt.test.util.authentication.GoogleTokenRetrieverUser.USER_1;
import static com.oakinvest.lt.test.util.authentication.GoogleTokenRetrieverUser.USER_2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * JUnit helper.
 */
public class JUnitHelper {

    /**
     * Google login URL.
     */
    protected static final String GOOGLE_LOGIN_URL = "/v1/login/google";

    /**
     * Dynamo DB Server.
     */
    private DynamoDBProxyServer server;

    /**
     * Mock mvc.
     */
    @Autowired
    private MockMvc mvc;

    /**
     * Google token retriever.
     */
    @Autowired
    private GoogleTokenRetriever googleTokenRetriever;

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
    @After
    public void stopDynamoDB() throws Exception {
        server.stop();
    }

    /**
     * Return a loose touch toker for a user.
     *
     * @param user USER_1 or USER_2.
     * @return token
     * @throws Exception exception
     */
    protected String getLooseToucheToken(final GoogleTokenRetrieverUser user) throws Exception {
        Optional<String> googleToken;
        if (USER_1 == user) {
            googleToken = googleTokenRetriever.getIdToken(USER_1);
        } else {
            googleToken = googleTokenRetriever.getIdToken(USER_2);
        }

        if (googleToken.isPresent()) {
            MvcResult result = mvc.perform(get(GOOGLE_LOGIN_URL)
                    .param("googleIdToken", googleToken.get()))
                    .andExpect(status().isOk())
                    .andReturn();
            return result.getResponse().getContentAsString();
        } else {
            return null;
        }

    }

    /**
     * Get mapper.
     *
     * @return mapper
     */
    protected final DynamoDBMapper getMapper() {
        return mapper;
    }

    /**
     * Get mvc.
     *
     * @return mvc
     */
    protected final MockMvc getMvc() {
        return mvc;
    }

}
