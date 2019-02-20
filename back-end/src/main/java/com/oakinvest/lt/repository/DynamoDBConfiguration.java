package com.oakinvest.lt.repository;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * DynamoDB Configuration.
 */
@Configuration
public class DynamoDBConfiguration {

    /**
     * Dynamo DB Server.
     */
    private DynamoDBProxyServer server;

    /**
     * Returns an amazonDynamoDB instance.
     *
     * @return amazonDynamoDB
     * @throws Exception exception
     */
    @Bean
    @SuppressWarnings("checkstyle:DesignForExtension")
    public AmazonDynamoDB amazonDynamoDB() throws Exception {
        System.setProperty("sqlite4java.library.path", "native-libs");
        final String[] localArgs = {"-sharedDb", "-inMemory"};
        DynamoDBProxyServer server = ServerRunner.createServerFromCommandLineArgs(localArgs);
        server.start();
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://127.0.0.1:8000/", "eu-west-3")).build();
        return dynamoDB;
    }

    /**
     * On application disposal.
     */
    @PreDestroy
    public void destroy() throws Exception {
        server.stop();
    }

}
