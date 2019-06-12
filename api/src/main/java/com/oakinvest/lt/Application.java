package com.oakinvest.lt;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.oakinvest.lt.domain.Contact;
import com.oakinvest.lt.domain.Account;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;

import static com.oakinvest.lt.configuration.Application.DYNAMODB_TABLE_CREATION_PARAMETER;
import static com.oakinvest.lt.configuration.Application.LOCAL_DYNAMODB_PARAMETER;

/**
 * Application.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    /**
     * Application run.
     *
     * @param args arguments
     * @throws Exception if dynamodb server start fails.
     */
    public static void main(final String[] args) throws Exception {
        // Start a local dynamodb server.
        if (Arrays.asList(args).contains(LOCAL_DYNAMODB_PARAMETER)) {
            System.out.println("=====> LOCAL_DYNAMODB_PARAMETER");

            // Start server.
            System.setProperty("sqlite4java.library.path", "native-libs");
            final String[] localArgs = {"-sharedDb", "-inMemory"};
            DynamoDBProxyServer server = ServerRunner.createServerFromCommandLineArgs(localArgs);
            server.start();
        }

        if (Arrays.asList(args).contains(LOCAL_DYNAMODB_PARAMETER) || Arrays.asList(args).contains(DYNAMODB_TABLE_CREATION_PARAMETER)) {
            System.out.println("=====> DYNAMODB_TABLE_CREATION_PARAMETER");

            // Initiate connexion.
            AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder
                                    .EndpointConfiguration("http://0.0.0.0:8000/", "eu-west-3"))
                    .build();
            DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

            ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(1L, 1L);

            // Creates the tables ACCOUNTS.
            CreateTableRequest createAccountsTableRequest = mapper.generateCreateTableRequest(Account.class)
                    .withProvisionedThroughput(provisionedThroughput);
            createAccountsTableRequest.getGlobalSecondaryIndexes().forEach(v -> {
                v.withProjection(new Projection().withProjectionType(ProjectionType.ALL));
                v.setProvisionedThroughput(provisionedThroughput);
            });
            dynamoDB.createTable(createAccountsTableRequest);

            // Creates the table CONTACTS.
            CreateTableRequest createContactsTableRequest = mapper.generateCreateTableRequest(Contact.class)
                    .withProvisionedThroughput(provisionedThroughput);
            createContactsTableRequest.getLocalSecondaryIndexes().forEach(v -> v.withProjection(new Projection().withProjectionType(ProjectionType.ALL)));
            dynamoDB.createTable(createContactsTableRequest);
        }

        SpringApplication.run(Application.class, args);
    }

    /**
     * Create required HandlerMapping, to avoid several default HandlerMapping instances being created.
     *
     * @return Handler mapping
     */
    @SuppressWarnings("DesignForExtension")
    @Bean
    public HandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    /**
     * Create required HandlerAdapter, to avoid several default HandlerAdapter instances being created.
     *
     * @return Handler adapter
     */
    @SuppressWarnings("DesignForExtension")
    @Bean
    public HandlerAdapter handlerAdapter() {
        return new RequestMappingHandlerAdapter();
    }

}
