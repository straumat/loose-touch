package com.oakinvest.lt.configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DynamoDB Configuration.
 */
@Configuration
public class DynamoDBConfiguration {

    /**
     * Returns an amazonDynamoDB instance.
     *
     * @return amazonDynamoDB
     */
    @Bean
    @SuppressWarnings("checkstyle:DesignForExtension")
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder
                                .EndpointConfiguration("http://127.0.0.1:8000/", "eu-west-3"))
                .build();
    }

}
