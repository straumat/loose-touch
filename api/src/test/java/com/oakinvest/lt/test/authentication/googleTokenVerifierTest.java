package com.oakinvest.lt.test.authentication;

import com.oakinvest.lt.Application;
import com.oakinvest.lt.authentication.google.GoogleTokenVerifier;
import com.oakinvest.lt.test.util.authentication.GoogleRefreshToken;
import com.oakinvest.lt.test.util.authentication.GoogleTokensRetriever;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static com.oakinvest.lt.configuration.Application.LOCAL_DYNAMODB_ENVIRONMENT;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_1;
import static com.oakinvest.lt.test.util.data.TestUsers.GOOGLE_USER_2;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Google token verifier test.
 */
@ActiveProfiles(LOCAL_DYNAMODB_ENVIRONMENT)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class googleTokenVerifierTest {

    /**
     * Google token verifier.
     */
    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    /**
     * Google token retriever.
     */
    @Autowired
    private GoogleTokensRetriever googleTokenRetriever;

    /**
     * Test with user 1.
     */
    @Test
    public void user1TokenVerificationTest() throws IOException {
        // Invalid token for user 1.
        final String user1InvalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        assertFalse("Invalid token was accepted", googleTokenVerifier.verifyToken(user1InvalidToken).isPresent());

        // Expired token for user 1.
        final String user1ExpiredToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjdjMzA5ZTNhMWMxOTk5Y2IwNDA0YWI3MTI1ZWU0MGI3Y2RiY2FmN2QiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDgzMTQyMTkxNDktNjBzOGwybHRyYmFsODJobnVqMzV1ODFvcHQyN2doc2EuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDgzMTQyMTkxNDktNjBzOGwybHRyYmFsODJobnVqMzV1ODFvcHQyN2doc2EuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTU3ODc3MTAxOTYwNjc0NzY4ODEiLCJlbWFpbCI6Imxvb3NlLnRvdWNoLnRlc3QuMkBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6IjRMb180U0w4MzZjSXUyMkxJcE9rUXciLCJuYW1lIjoibG9vc2UgdG91Y2ggMiIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vLUd4bWpaUEY0VEk4L0FBQUFBQUFBQUFJL0FBQUFBQUFBQUFBL0FDZXZvUU1NZENhZklIM1hkOTdEUmwxbmJRdGJ2dWlqQWcvczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6Imxvb3NlIiwiZmFtaWx5X25hbWUiOiJ0b3VjaCAyIiwibG9jYWxlIjoiZnIiLCJpYXQiOjE1NTAyMzI2MDksImV4cCI6MTU1MDIzNjIwOX0.vLeSIFRUtHJtiy58682xZOvIO3hPwuTGR0VTEoSHvwyT3yBtaOHYWPum4CTMbUxiBq6hupPJoJ3IMVbgDxLs0PfnYxQDrow50qaC5Lcio-BsTy2xJxnL1kCFILs6AYVnDEBhaGQ57M6u8BAKnaap2oOhK20x7I9n4hcxqba0dEqOkuy4NPgCe3UoXgg6hS8-XhEEzs7rPoMrzFvnz6HZANmU0GSNdI94xSG_a9PqYddBcjBLdWgEKshywlnKM7T5FvXq2Uo2Hl_wtp8qYFI2lDjGJdRzTBQ7VyU3BPNgkDB0ReGoOeT1webGSSCc8_txc7F_DIgv90KfvwbDNxEusg";
        assertFalse("Expired token was accepted", googleTokenVerifier.verifyToken(user1ExpiredToken).isPresent());

        // getting a new token from google.
        Optional<GoogleRefreshToken> token = googleTokenRetriever.getIdToken(GOOGLE_USER_1);
        assertTrue("No token was given by google", token.isPresent());
    }

    /**
     * Test with user 2.
     */
    @Test
    public void user2TokenVerificationTest() throws IOException {
        // Invalid token for user 2.
        final String user2InvalidToken = "eyJhbGciOiJIUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.bQTnz6AuMJvmXXQsVPrxeQNvzDkimo7VNXxHeSBfClLufmCVZRUuyTwJF311JHuh";
        assertFalse("Invalid token was accepted", googleTokenVerifier.verifyToken(user2InvalidToken).isPresent());

        //Expired token for user 2.
        final String user2ExpiredToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjdjMzA5ZTNhMWMxOTk5Y2IwNDA0YWI3MTI1ZWU0MGI3Y2RiY2FmN2QiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDgzMTQyMTkxNDktNjBzOGwybHRyYmFsODJobnVqMzV1ODFvcHQyN2doc2EuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDgzMTQyMTkxNDktNjBzOGwybHRyYmFsODJobnVqMzV1ODFvcHQyN2doc2EuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTU3ODc3MTAxOTYwNjc0NzY4ODEiLCJlbWFpbCI6Imxvb3NlLnRvdWNoLnRlc3QuMkBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6IjRMb180U0w4MzZjSXUyMkxJcE9rUXciLCJuYW1lIjoibG9vc2UgdG91Y2ggMiIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vLUd4bWpaUEY0VEk4L0FBQUFBQUFBQUFJL0FBQUFBQUFBQUFBL0FDZXZvUU1NZENhZklIM1hkOTdEUmwxbmJRdGJ2dWlqQWcvczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6Imxvb3NlIiwiZmFtaWx5X25hbWUiOiJ0b3VjaCAyIiwibG9jYWxlIjoiZnIiLCJpYXQiOjE1NTAyMzI2MDksImV4cCI6MTU1MDIzNjIwOX0.vLeSIFRUtHJtiy58682xZOvIO3hPwuTGR0VTEoSHvwyT3yBtaOHYWPum4CTMbUxiBq6hupPJoJ3IMVbgDxLs0PfnYxQDrow50qaC5Lcio-BsTy2xJxnL1kCFILs6AYVnDEBhaGQ57M6u8BAKnaap2oOhK20x7I9n4hcxqba0dEqOkuy4NPgCe3UoXgg6hS8-XhEEzs7rPoMrzFvnz6HZANmU0GSNdI94xSG_a9PqYddBcjBLdWgEKshywlnKM7T5FvXq2Uo2Hl_wtp8qYFI2lDjGJdRzTBQ7VyU3BPNgkDB0ReGoOeT1webGSSCc8_txc7F_DIgv90KfvwbDNxEusg";
        assertFalse("Expired token was accepted", googleTokenVerifier.verifyToken(user2ExpiredToken).isPresent());

        // getting a new token from google.
        Optional<GoogleRefreshToken> token = googleTokenRetriever.getIdToken(GOOGLE_USER_2);
        assertTrue("No token was given by google", token.isPresent());
    }

}
