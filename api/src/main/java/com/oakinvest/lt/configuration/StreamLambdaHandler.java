package com.oakinvest.lt.configuration;


import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.oakinvest.lt.Application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Stream lambda HANDLER.
 */
public class StreamLambdaHandler implements RequestStreamHandler {

    /**
     * Handler.
     */
    private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> HANDLER;

    static {
        try {
            HANDLER = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application.class);
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    /**
     * Handle request.
     *
     * @param inputStream  input stream
     * @param outputStream output stream
     * @param context      context
     * @throws IOException exception.
     */
    @SuppressWarnings("DesignForExtension")
    @Override
    public void handleRequest(final InputStream inputStream, final OutputStream outputStream, final Context context) throws IOException {
        HANDLER.proxyStream(inputStream, outputStream, context);
    }

}