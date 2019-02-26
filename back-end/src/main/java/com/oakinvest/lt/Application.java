package com.oakinvest.lt;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.oakinvest.lt.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;

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

            // Start server.
            System.setProperty("sqlite4java.library.path", "native-libs");
            final String[] localArgs = {"-sharedDb", "-inMemory"};
            DynamoDBProxyServer server = ServerRunner.createServerFromCommandLineArgs(localArgs);
            server.start();

            // Initiate connexion.
            AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder
                                    .EndpointConfiguration("http://127.0.0.1:8000/", "eu-west-3"))
                    .build();
            DynamoDBMapper mapper = new DynamoDBMapper(dynamoDB);

            // Creates the tables.
            ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(1L, 1L);
            CreateTableRequest createUserTableRequest = mapper.generateCreateTableRequest(User.class)
                    .withProvisionedThroughput(provisionedThroughput);
            createUserTableRequest.getGlobalSecondaryIndexes().forEach(v -> v.setProvisionedThroughput(provisionedThroughput));
            dynamoDB.createTable(createUserTableRequest);

            // Waits for every table to be created.
            TableUtils.waitUntilActive(dynamoDB, createUserTableRequest.getTableName());
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