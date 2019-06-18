package com.oakinvest.lt.configuration;

import com.oakinvest.lt.authentication.loosetouch.AuthenticationAccountResolver;
import com.oakinvest.lt.authentication.loosetouch.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

    @Override
    public final void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedMethods("PUT", "GET", "DELETE", "OPTIONS", "PATCH", "POST")
                .allowedOrigins("*")
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type", "Accept", "X-Requested-With", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Origin")
                .exposedHeaders("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Origin", "Access-Control-Expose-Headers");
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
     * Returns authentication account resolver.
     *
     * @return authentication account resolver
     */
    @Bean
    @SuppressWarnings("checkstyle:DesignForExtension")
    public AuthenticationAccountResolver authenticationAccountResolver() {
        return new AuthenticationAccountResolver();
    }

    @Override
    public final void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(authenticationAccountResolver());
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
