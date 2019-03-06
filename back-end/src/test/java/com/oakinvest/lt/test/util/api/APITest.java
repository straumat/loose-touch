package com.oakinvest.lt.test.util.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.oakinvest.lt.authentication.loosetouch.LooseTouchTokenProvider;
import com.oakinvest.lt.domain.Contact;
import com.oakinvest.lt.domain.User;
import com.oakinvest.lt.repository.ContactRepository;
import com.oakinvest.lt.repository.UserRepository;
import com.oakinvest.lt.test.util.authentication.GoogleRefreshToken;
import com.oakinvest.lt.test.util.authentication.GoogleTokensRetriever;
import com.oakinvest.lt.test.util.data.TestUsers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static com.oakinvest.lt.configuration.Application.LOCAL_DYNAMODB_ENVIRONMENT;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_1;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API Test.
 */
@ActiveProfiles(LOCAL_DYNAMODB_ENVIRONMENT)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class APITest {

    /**
     * Google login URL.
     */
    protected static final String GOOGLE_LOGIN_URL = "/v1/login/google";

    /**
     * Profile URL.
     */
    protected static final String PROFILE_URL = "/v1/user/profile";

    /**
     * Delete user URL.
     */
    protected static final String DELETE_USER_URL = "/v1/user/delete";

    /**
     * Contact URL.
     */
    protected static final String CONTACT_URL = "/v1/contacts";

    /**
     * Date format used everywhere.
     */
    protected static final String DATE_FORMAT = "dd/MM/yyyy";

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
    private GoogleTokensRetriever googleTokenRetriever;

    /**
     * Loose touch token provider.
     */
    @Autowired
    private LooseTouchTokenProvider looseTouchTokenProvider;

    /**
     * DynamoDB connection.
     */
    @Autowired
    private AmazonDynamoDB dynamoDB;

    /**
     * User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Contact repository.
     */
    @Autowired
    private ContactRepository contactRepository;

    /**
     * Object mapper.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Authentication test.
     *
     * @throws Exception error
     */
    @Test
    public abstract void authenticationTest() throws Exception;

    /**
     * Valid data test.
     *
     * @throws Exception error
     */
    @Test
    public abstract void validDataTest() throws Exception;

    /**
     * Business logic test.
     *
     * @throws Exception error
     */
    @Test
    public abstract void businessLogicTest() throws Exception;

    /**
     * Start DynamoDB.
     *
     * @throws Exception error starting DynamoDB
     */
    @Before
    public void startDynamoDB() throws Exception {
        System.setProperty("sqlite4java.library.path", "native-libs");
        final String[] localArgs = {"-sharedDb", "-inMemory"};
        server = ServerRunner.createServerFromCommandLineArgs(localArgs);
        server.start();

        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(5L, 5L);

        // Creates the table USERS.
        CreateTableRequest createUsersTableRequest = mapper.generateCreateTableRequest(User.class)
                .withProvisionedThroughput(provisionedThroughput);
        createUsersTableRequest.getGlobalSecondaryIndexes().forEach(v -> {
            v.withProjection(new Projection().withProjectionType(ProjectionType.ALL));
            v.setProvisionedThroughput(provisionedThroughput);
        });
        dynamoDB.createTable(createUsersTableRequest);

        // Creates the table CONTACTS.
        CreateTableRequest createContactsTableRequest = mapper.generateCreateTableRequest(Contact.class).withProvisionedThroughput(provisionedThroughput);
        dynamoDB.createTable(createContactsTableRequest);

        // Waits for every table to be created.
        TableUtils.waitUntilActive(dynamoDB, createUsersTableRequest.getTableName());
        TableUtils.waitUntilActive(dynamoDB, createContactsTableRequest.getTableName());
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
     * Return a loose touch for a user.
     *
     * @param user GOOGLE_USER_1 or GOOGLE_USER_2.
     * @return token
     * @throws Exception exception
     */
    protected String getLooseToucheToken(final TestUsers user) throws Exception {
        Optional<GoogleRefreshToken> googleToken;
        if (GOOGLE_USER_1 == user) {
            googleToken = googleTokenRetriever.getIdToken(GOOGLE_USER_1);
        } else {
            googleToken = googleTokenRetriever.getIdToken(GOOGLE_USER_2);
        }

        if (googleToken.isPresent()) {
            MvcResult result = mvc.perform(get(GOOGLE_LOGIN_URL)
                    .param("googleIdToken", googleToken.get().getIdToken())
                    .param("googleAccessToken", googleToken.get().getAccessToken()))
                    .andExpect(status().isOk())
                    .andReturn();
            return JsonPath.parse(result.getResponse().getContentAsString()).read("idToken").toString();
        } else {
            return null;
        }
    }

    /**
     * Get mvc.
     *
     * @return mvc
     */
    public final MockMvc getMvc() {
        return mvc;
    }

    /**
     * Get googleTokenRetriever.
     *
     * @return googleTokenRetriever
     */
    public final GoogleTokensRetriever getGoogleTokenRetriever() {
        return googleTokenRetriever;
    }

    /**
     * Get looseTouchTokenProvider.
     *
     * @return looseTouchTokenProvider
     */
    public final LooseTouchTokenProvider getLooseTouchTokenProvider() {
        return looseTouchTokenProvider;
    }

    /**
     * Get dynamoDB.
     *
     * @return dynamoDB
     */
    public final AmazonDynamoDB getDynamoDB() {
        return dynamoDB;
    }

    /**
     * Get userRepository.
     *
     * @return userRepository
     */
    public final UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * Get contactRepository.
     *
     * @return contactRepository
     */
    public final ContactRepository getContactRepository() {
        return contactRepository;
    }

    /**
     * Get mapper.
     *
     * @return mapper
     */
    public final ObjectMapper getMapper() {
        return mapper;
    }

}
