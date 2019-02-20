package com.oakinvest.lt.test.authentication;

import com.oakinvest.lt.Application;
import com.oakinvest.lt.authentication.loosetouch.LooseTouchTokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * JwtTokenProvider test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class LooseTouchTokenProviderTest {

    /**
     * JWT Token provider.
     */
    @Autowired
    private LooseTouchTokenProvider looseTouchTokenProvider;

    /**
     * Testing token creation.
     */
    @Test
    public final void tokenCreationTest() {
        final String testUserId = "straumat";

        // Token creation.
        final String token = looseTouchTokenProvider.createToken(testUserId);

        // userId retrieval.
        Optional<String> userId = looseTouchTokenProvider.getUserId(token);
        assertTrue("userId is not present", userId.isPresent());
        assertEquals("Wrong user id retrieved from token", testUserId, userId.get());
    }

    /**
     * Testing that a non valid token is rejected.
     */
    @Test
    public final void tokenValidityTest() {
        final String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        // userId retrieval.
        Optional<String> userId = looseTouchTokenProvider.getUserId(invalidToken);
        assertFalse("userId was retrieved from an invalid token", userId.isPresent());
    }

    /**
     * Testing that token with a delay expired is rejected.
     * @throws InterruptedException if sleep fails
     */
    @Test
    public final void tokenExpirationDelayTest() throws InterruptedException {
        final String testUserId = "straumat";

        // Token creation with an expiration delay of 2 seconds.
        final String token = looseTouchTokenProvider.createToken(testUserId, TimeUnit.SECONDS.toMillis(2));

        // userId retrieval.
        Optional<String> userId = looseTouchTokenProvider.getUserId(token);
        assertTrue("userId is not present", userId.isPresent());

        // We wait until the delay is expired.
        Thread.sleep(TimeUnit.SECONDS.toMillis(2));

        // userId retrieval.
        userId = looseTouchTokenProvider.getUserId(token);
        assertFalse("userId is present", userId.isPresent());
    }

    /**
     * Testing that an empty token is accepted.
     */
    @Test
    public final void testEmptyToken() {
        Optional<String> userId = looseTouchTokenProvider.getUserId(null);
        assertFalse("userId is not present", userId.isPresent());
    }

    /**
     * Test that we can create token with user ID generated for dynamo db.
     */
    @Test
    public final void testDynamoDBUserToken() {
        assertNotNull("Fail creating token", looseTouchTokenProvider.createToken("520a2c29-0793-42c2-885e-982663aecba7"));
    }

}
