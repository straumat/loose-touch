package com.oakinvest.lt.configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
     * Environment name : staging.
     */
    private static final String STAGING_ENVIRONMENT_NAME = "staging";

    /**
     * Environment name : production.
     */
    private static final String PRODUCTION_ENVIRONMENT_NAME = "staging";

    /**
     * Table prefix for staging environment.
     */
    private static final String STAGING_ENVIRONMENT_PREFIX = "STAGING_";

    /**
     * Table prefix for production environment.
     */
    private static final String PRODUCTION_ENVIRONMENT_PREFIX = "PRODUCTION_";

    /**
     * Environment.
     */
    @Autowired
    private Environment springEnvironment;

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
     * Get the stage from springEnvironment.
     */
    @Value("#{environment.environment}")
    private String environment;

    /**
     * Returns an amazonDynamoDB instance.
     *
     * @return amazonDynamoDB
     */
    @Bean
    @SuppressWarnings("checkstyle:DesignForExtension")
    public AmazonDynamoDB amazonDynamoDB() {
        if (Arrays.asList(springEnvironment.getActiveProfiles()).contains(LOCAL_DYNAMODB_ENVIRONMENT)) {
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
     * Returns dynamodb mapper.
     *
     * @return dynamodb mapper.
     */
    @Bean
    @SuppressWarnings("checkstyle:DesignForExtension")
    public DynamoDBMapper dynamoDBMapper() {
        // Staging environment.
        if (STAGING_ENVIRONMENT_NAME.equalsIgnoreCase(environment)) {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig.Builder()
                    .withTableNameOverride(DynamoDBMapperConfig
                            .TableNameOverride.withTableNamePrefix(STAGING_ENVIRONMENT_PREFIX))
                    .build();
            return new DynamoDBMapper(amazonDynamoDB(), config);
        } else if (PRODUCTION_ENVIRONMENT_NAME.equalsIgnoreCase(environment)) {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig.Builder()
                    .withTableNameOverride(DynamoDBMapperConfig
                            .TableNameOverride.withTableNamePrefix(PRODUCTION_ENVIRONMENT_PREFIX))
                    .build();
            return new DynamoDBMapper(amazonDynamoDB(), config);
        } else {
            return new DynamoDBMapper(amazonDynamoDB());
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
