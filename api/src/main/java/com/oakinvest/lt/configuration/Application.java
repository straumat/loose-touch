package com.oakinvest.lt.configuration;

/**
 * Application constants.
 */
public final class Application {

    /**
     * Private constructor.
     */
    private Application() {
    }

    /**
     * Parameter for local DynamoDB.
     */
    public static final String LOCAL_DYNAMODB_PARAMETER = "--start-local-dynamodb";

    /**
     * Parameter that indicates that we should create the tables.
     */
    public static final String DYNAMODB_TABLE_CREATION_PARAMETER = "--create-dynamodb-tables";

    /**
     * Environment for local DynamoDB.
     */
    public static final String LOCAL_DYNAMODB_ENVIRONMENT = "local-dynamodb";

    /**
     * Parameter containing account id.
     */
    public static final String ACCOUNT_ID_PARAMETER = "accountId";

    /**
     * Parameter containing loose touch token.
     */
    public static final String LOOSE_TOUCH_TOKEN_PARAMETER = "looseTouchToken";

    /**
     * Bearer type.
     */
    public static final String BEARER_TYPE = "Bearer";

}
