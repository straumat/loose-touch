package com.oakinvest.lt;

import com.oakinvest.lt.controller.PingController;
import com.oakinvest.lt.util.auth.google.GoogleTokenVerifier;
import com.oakinvest.lt.util.auth.loosetouch.JwtTokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Application.
 */
@SpringBootApplication
@Import({PingController.class, GoogleTokenVerifier.class, JwtTokenProvider.class})
public class Application extends SpringBootServletInitializer {

    /**
     * Application run.
     *
     * @param args arguments
     */
    public static void main(final String[] args) {
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