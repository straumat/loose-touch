package com.oakinvest.lt.configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

import static com.oakinvest.lt.configuration.Application.LOCAL_DYNAMODB_ENVIRONMENT;

/**
 * DynamoDB Configuration.
 */
@Configuration
public class DynamoDB {

    /**
     * Application arguments.
     */
    @Autowired
    private ApplicationArguments applicationArguments;

    /**
     * Environment.
     */
    @Autowired
    private Environment environment;

    /**
     * DynamoDB Endpoint.
     */
    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    /**
     * AWS Region.
     */
    @Value("${amazon.aws.region}")
    private String amazonAWSRegion;

    /**
     * AWS Access key.
     */
    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    /**
     * AWS Secret key.
     */
    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    /**
     * Returns an amazonDynamoDB instance.
     *
     * @return amazonDynamoDB
     */
    @Bean
    @SuppressWarnings("checkstyle:DesignForExtension")
    public AmazonDynamoDB amazonDynamoDB() {

        if (Arrays.asList(environment.getActiveProfiles()).contains(LOCAL_DYNAMODB_ENVIRONMENT)) {
            // Running on local DynamoDB.
            return AmazonDynamoDBClientBuilder.standard()
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, amazonAWSRegion))
                    .build();
        } else {
            // Running on AWS.
            return AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(amazonAWSCredentialsProvider())
                    .withRegion(amazonAWSRegion)
                    .build();
        }

    }

    /**
     * Credentials.
     *
     * @return credentials
     */
    private AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey));
    }

}
