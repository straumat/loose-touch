package com.oakinvest.lt.configuration;

import com.oakinvest.lt.authentication.loosetouch.AuthenticationInterceptor;
import com.oakinvest.lt.authentication.loosetouch.AuthenticationUserResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web configuration.
 */
@Configuration
@EnableWebMvc
public class Spring implements WebMvcConfigurer {

    @Override
    public final void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/v1/**/*")
                .excludePathPatterns("/v1/login/*");
    }

    /**
     * Returns authentication interceptor.
     *
     * @return authentication interceptor
     */
    @Bean
    @SuppressWarnings("checkstyle:DesignForExtension")
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    /**
     * Returns authentication user resolver.
     *
     * @return authentication user resolver
     */
    @Bean
    @SuppressWarnings("checkstyle:DesignForExtension")
    public AuthenticationUserResolver authenticationUserResolver() {
        return new AuthenticationUserResolver();
    }

    @Override
    public final void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(authenticationUserResolver());
    }

    @Override
    public final void configurePathMatch(final PathMatchConfigurer configurer) {
        // turn off all suffix pattern matching
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public final void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

}
