package com.oakinvest.lt.test.util.authentication;

import com.oakinvest.lt.test.util.data.TestUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_1;

/**
 * Google token retriever.
 */
@Component
public class GoogleTokensRetriever {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(GoogleTokensRetriever.class);

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
    @Value("${application.test.user1.refreshToken}")
    private String user1GoogleRefreshToken;

    /**
     * Google refresh token for test user 2 (loose.touch.test.2@gmail.com).
     */
    @Value("${application.test.user2.refreshToken}")
    private String user2GoogleRefreshToken;

    /**
     * File name of the file where is stored google tokens for test user 1 (loose.touch.test.1@gmail.com).
     */
    private static final String USER_1_GOOGLE_TOKENS_FILE_NAME = "google-id-token-test-1.ser";

    /**
     * File name of the file where is stored google tokens for test user 2 (loose.touch.test.2@gmail.com).
     */
    private static final String USER_2_GOOGLE_TOKENS_FILE_NAME = "google-id-token-test-2.ser";

    /**
     * Retrieve an id ticket for a given user.
     *
     * @param user user (GOOGLE_USER_1 or GOOGLE_USER_2).
     * @return google id token.
     */
    public Optional<GoogleRefreshToken> getIdToken(final TestUsers user) throws IOException {
        log.info("Asking for a token for user " + user);
        Optional<GoogleRefreshToken> idToken = getIdTokenFromCache(user);

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
                log.info("Impossible to getContact a token for user " + user + " from Google");
            }
            return idToken;
        }
    }

    /**
     * Retrieve an id ticket from cache for a given user.
     *
     * @param user user (GOOGLE_USER_1 or GOOGLE_USER_2).
     * @return google id token.
     */
    private Optional<GoogleRefreshToken> getIdTokenFromCache(final TestUsers user) throws IOException {
        // File containing the token.
        File googleIdTokenFile;
        if (user.equals(GOOGLE_USER_1)) {
            googleIdTokenFile = new File(USER_1_GOOGLE_TOKENS_FILE_NAME);
        } else {
            googleIdTokenFile = new File(USER_2_GOOGLE_TOKENS_FILE_NAME);
        }

        // If the file exists.
        if (googleIdTokenFile.exists()) {

            // If the file is older than google id token expiration, we delete it.
            BasicFileAttributes fileAttributes = Files.readAttributes(googleIdTokenFile.toPath(), BasicFileAttributes.class);
            final long fileCreationDate = fileAttributes.creationTime().toMillis();
            final long currentDate = Calendar.getInstance().getTimeInMillis();
            final long delaySinceFileCreation = currentDate - fileCreationDate;
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
                return Optional.of((GoogleRefreshToken) loadObjectFromFile(googleIdTokenFile));
            }
        } else {
            // No file.
            return Optional.empty();
        }
    }

    /**
     * Retrieve an id ticket from google for a given user.
     *
     * @param user user (GOOGLE_USER_1 or GOOGLE_USER_2).
     * @return google id token.
     */
    private Optional<GoogleRefreshToken> getIdTokenFromGoogle(final TestUsers user) {
        log.info("Saving google id token for " + user);

        // Refresh token.
        String refreshToken;
        if (user.equals(GOOGLE_USER_1)) {
            refreshToken = user1GoogleRefreshToken;
        } else {
            refreshToken = user2GoogleRefreshToken;
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
            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Save a token in cache
     *
     * @param user   user (GOOGLE_USER_1 or GOOGLE_USER_2).
     * @param tokens tokens to save
     */
    private void saveTokenInCache(final TestUsers user, final GoogleRefreshToken tokens) throws IOException {
        log.info("Saving google tokens in cache for user " + user);

        // File containing the token.
        String path;
        if (user.equals(GOOGLE_USER_1)) {
            path = USER_1_GOOGLE_TOKENS_FILE_NAME;
        } else {
            path = USER_2_GOOGLE_TOKENS_FILE_NAME;
        }

        // Writing
        writeObjectToFile(path, tokens);
    }

    /**
     * Write an object to a file (serialize).
     *
     * @param path path to file
     * @param o    object
     */
    private void writeObjectToFile(final String path, final Object o) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(o);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            log.error("Error (IOException) : " + e.getMessage(), e);
        }
    }

    /**
     * Load an object from a file (deserialize).
     *
     * @param file file
     * @return the object
     */
    private Object loadObjectFromFile(final File file) {
        Object o = null;
        try {
            FileInputStream fileIn = new FileInputStream(file.getPath());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            o = in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {
            log.error("Error (IOException) : " + e.getMessage(), e);
        } catch (ClassNotFoundException c) {
            log.error("Error (ClassNotFoundException) : " + c.getMessage(), c);
        }
        return o;
    }

}
