package com.oakinvest.lt.test.util.junit;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_1;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * JUnit helper.
 */
public class JUnitHelper {

    /**
     * Google login URL.
     */
    private static final String GOOGLE_LOGIN_URL = "/v1/login/google";

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
     * Loose touch token provider.
     */
    @Autowired
    private LooseTouchTokenProvider looseTouchTokenProvider;

    /**
     * Object mapper.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Start DynamoDB.
     */
    @Before
    public void startDynamoDB() throws Exception {
        System.setProperty("sqlite4java.library.path", "native-libs");
        final String[] localArgs = {"-sharedDb", "-inMemory"};
        server = ServerRunner.createServerFromCommandLineArgs(localArgs);
        server.start();

        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(5L, 5L);

        // Creates the tables USERS.
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
     * Return a loose touch toker for a user.
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
    protected final MockMvc getMvc() {
        return mvc;
    }

    /**
     * Get userRepository.
     *
     * @return userRepository
     */
    protected final UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * Get contactRepository.
     *
     * @return contactRepository
     */
    protected final ContactRepository getContactRepository() {
        return contactRepository;
    }

    /**
     * Get googleTokenRetriever.
     *
     * @return googleTokenRetriever
     */
    protected final GoogleTokensRetriever getGoogleTokenRetriever() {
        return googleTokenRetriever;
    }

    /**
     * Get looseTouchTokenProvider.
     *
     * @return looseTouchTokenProvider
     */
    protected final LooseTouchTokenProvider getLooseTouchTokenProvider() {
        return looseTouchTokenProvider;
    }

    /**
     * Get mapper.
     *
     * @return mapper
     */
    protected final ObjectMapper getMapper() {
        return mapper;
    }

}
