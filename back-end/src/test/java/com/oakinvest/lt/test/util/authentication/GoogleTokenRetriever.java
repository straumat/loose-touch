package com.oakinvest.lt.test.util.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.oakinvest.lt.test.util.authentication.GoogleTokenRetrieverUser.USER_1;

/**
 * Google token retriever.
 */
@Component
public class GoogleTokenRetriever {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(GoogleTokenRetriever.class);

    /**
     * Client id.
     */
    @Value("${security.oauth2.client.clientId}")
    private String clientId;

    /**
     * Client secret.
     */
    @Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    /**
     * Access token url.
     */
    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;

    /**
     * Google refresh token for test user 1 (loose.touch.test.1@gmail.com).
     */
    private static final String USER_1_GOOGLE_REFRESH_TOKEN = "1/IMZ1k7G6ksE71CrvuaLXyKXswIIseVo039wv1cSwzY4";

    /**
     * File name of the file where is stored google id token for test user 1 (loose.touch.test.1@gmail.com).
     */
    private static final String USER_1_GOOGLE_ID_TOKEN_FILE_NAME = "google-id-token-test-1.txt";

    /**
     * Google refresh token for test user 2 (loose.touch.test.2@gmail.com).
     */
    private static final String USER_2_GOOGLE_REFRESH_TOKEN = "1/2WbPyTeIpT2CH7744KE77WgcCWBxPjZkGRKYM2EUsWsMScMFzyZq0GmGy2WONcBp";

    /**
     * File name of the file where is stored google id token for test user 2 (loose.touch.test.2@gmail.com).
     */
    private static final String USER_2_GOOGLE_ID_TOKEN_FILE_NAME = "google-id-token-test-2.txt";

    /**
     * Retrieve an id ticket for a given user.
     * @param user user (USER_1 or USER_2).
     * @return google id token.
     */
    public Optional<String> getIdToken(final GoogleTokenRetrieverUser user) throws IOException {
        log.info("Asking for a token for user " + user);
        Optional<String> idToken = getIdTokenFromCache(user);

        if (idToken.isPresent()) {
            log.info("Token for user " + user + " found in cache : " + idToken.get());
            return idToken;
        } else {
            log.info("Token for user " + user + " not present in cache.");
            idToken = getIdTokenFromGoogle(user);
            if (idToken.isPresent()) {
                log.info("Token for user " + user + " returned by google : " + idToken.get());
                saveTokenInCache(user, idToken.get());
            } else {
                log.info("Impossible to get a token for user " + user + " from Google");
            }
            return idToken;
        }
    }

    /**
     * Retrieve an id ticket from cache for a given user.
     * @param user user (USER_1 or USER_2).
     * @return google id token.
     */
    private Optional<String>getIdTokenFromCache(final GoogleTokenRetrieverUser user) throws IOException {
        // File containing the token.
        File googleIdTokenFile;
        if (user.equals(USER_1)) {
            googleIdTokenFile = new File(USER_1_GOOGLE_ID_TOKEN_FILE_NAME);
        } else {
            googleIdTokenFile = new File(USER_2_GOOGLE_ID_TOKEN_FILE_NAME);
        }

        // If the file exists.
        if (googleIdTokenFile.exists()) {

            // If the file is older than google id token expiration, we delete it.
            BasicFileAttributes fileAttributes = Files.readAttributes(googleIdTokenFile.toPath(), BasicFileAttributes.class);
            final long fileCreationDate = fileAttributes.creationTime().toMillis();
            final long currentDate =  Calendar.getInstance().getTimeInMillis();
            final long delaySinceFileCreation = currentDate-fileCreationDate;
            log.info("Google token file is " + TimeUnit.MILLISECONDS.toMinutes(delaySinceFileCreation) + " minutes old");

            if (TimeUnit.MILLISECONDS.toHours(delaySinceFileCreation) >= 1) {
                // We delete it.
                if (googleIdTokenFile.delete()) {
                    log.info("Google token file deleted");
                }
                // No file
                return Optional.empty();
            } else {
                // We return the content.
                return Optional.of(Files.readAllLines(googleIdTokenFile.toPath()).get(0));
            }
        } else {
            // No file.
            return Optional.empty();
        }
    }

    /**
     * Retrieve an id ticket from google for a given user.
     * @param user user (USER_1 or USER_2).
     * @return google id token.
     */
    public Optional<String>getIdTokenFromGoogle(final GoogleTokenRetrieverUser user) {
        log.info("Saving google id token for " + user);

        // Refresh token.
        String refreshToken;
        if (user.equals(USER_1)) {
            refreshToken = USER_1_GOOGLE_REFRESH_TOKEN;
        } else {
            refreshToken = USER_2_GOOGLE_REFRESH_TOKEN;
        }

        // Headers.
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        headers.setAll(map);

        // Parameters.
        Map<String, String> payload = new HashMap<>();
        payload.put("client_id", clientId);
        payload.put("client_secret", clientSecret);
        payload.put("refresh_token", refreshToken);
        payload.put("grant_type", "refresh_token");

        // Calling google.
        HttpEntity<?> request = new HttpEntity<>(payload, headers);
        GoogleRefreshToken result = new RestTemplate().postForObject(accessTokenUri, request, GoogleRefreshToken.class);

        if (result != null) {
            return Optional.of(result.getIdToken());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Save a token in cache
     * @param user user (USER_1 or USER_2).
     * @param idToken google id token
     */
    private void saveTokenInCache(final GoogleTokenRetrieverUser user, final String idToken) throws IOException {
        log.info("Saving google id in file for user " + user);

        // File containing the token.
        Path googleIdTokenFile;
        if (user.equals(USER_1)) {
            googleIdTokenFile = Paths.get(USER_1_GOOGLE_ID_TOKEN_FILE_NAME);
        } else {
            googleIdTokenFile = Paths.get(USER_2_GOOGLE_ID_TOKEN_FILE_NAME);
        }

        // Writing
        List<String> lines = Collections.singletonList(idToken);
        Files.write(googleIdTokenFile, lines, Charset.forName("UTF-8"));
    }

}
