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
     * Environment for local DynamoDB.
     */
    public static final String LOCAL_DYNAMODB_ENVIRONMENT = "local-dynamodb";

    /**
     * Parameter containing user id.
     */
    public static final String USER_ID_PARAMETER = "userId";

    /**
     * Parameter containing loose touch token.
     */
    public static final String LOOSE_TOUCH_TOKEN_PARAMETER = "looseTouchToken";

    /**
     * Bearer type.
     */
    public static final String BEARER_TYPE = "Bearer";

}
