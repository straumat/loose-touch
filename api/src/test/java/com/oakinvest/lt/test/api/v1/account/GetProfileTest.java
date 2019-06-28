package com.oakinvest.lt.test.api.v1.account;

import com.jayway.jsonpath.JsonPath;
import com.oakinvest.lt.test.util.api.APITest;
import org.junit.Ignore;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.TimeUnit;

import static com.oakinvest.lt.test.util.data.TestAccounts.GOOGLE_ACCOUNT_1;
import static com.oakinvest.lt.test.util.data.TestAccounts.GOOGLE_ACCOUNT_2;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Get profile test.
 */
public class GetProfileTest extends APITest {

    @Override
    public void authenticationTest() throws Exception {
        // No google token provided.
        getMockMvc().perform(get(ACCOUNT_PROFILE_URL)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("type").value(invalid_request_error.toString()))
                .andExpect(jsonPath("message").value("Loose touch token missing"))
                .andExpect(jsonPath("errors", hasSize(0)));

        // Dummy bearer.
        getMockMvc().perform(get(ACCOUNT_PROFILE_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("type").value(authentication_error.toString()))
                .andExpect(jsonPath("message").value("Invalid loose touch token : invalidToken"))
                .andExpect(jsonPath("errors", hasSize(0)));
    }

    @Ignore("No data to validate")
    @Override
    public void validDataTest() {

    }

    @Override
    public void businessLogicTest() throws Exception {
        // Getting account 1 profile.
        MvcResult result1 = getMockMvc().perform(get(ACCOUNT_PROFILE_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getLooseToucheToken(GOOGLE_ACCOUNT_1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idToken").isNotEmpty())
                .andExpect(jsonPath("firstName").value(GOOGLE_ACCOUNT_1.getFirstName()))
                .andExpect(jsonPath("lastName").value(GOOGLE_ACCOUNT_1.getLastName()))
                .andExpect(jsonPath("email").value(GOOGLE_ACCOUNT_1.getEmail()))
                .andExpect(jsonPath("pictureUrl").isString())
                .andExpect(jsonPath("newAccount").value(false))
                .andReturn();

        // Check that the returned token is ok and it's the good account.
        String looseTouchAccount1Token = JsonPath.parse(result1.getResponse().getContentAsString()).read("idToken").toString();
        assertTrue("Invalid token", getLooseTouchTokenProvider().getAccountId(looseTouchAccount1Token).isPresent());
        assertTrue("Invalid account", getAccountRepository().getAccount(getLooseTouchTokenProvider().getAccountId(looseTouchAccount1Token).get()).isPresent());

        // Getting account 2 profile.
        MvcResult result2 = getMockMvc().perform(get(ACCOUNT_PROFILE_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getLooseToucheToken(GOOGLE_ACCOUNT_2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idToken").isNotEmpty())
                .andExpect(jsonPath("firstName").value(GOOGLE_ACCOUNT_2.getFirstName()))
                .andExpect(jsonPath("lastName").value(GOOGLE_ACCOUNT_2.getLastName()))
                .andExpect(jsonPath("email").value(GOOGLE_ACCOUNT_2.getEmail()))
                .andExpect(jsonPath("pictureUrl").isString())
                .andExpect(jsonPath("newAccount").value(false))
                .andReturn();

        // Check that the returned token is ok and it's the good account.
        String looseTouchAccount2Token1 = JsonPath.parse(result2.getResponse().getContentAsString()).read("idToken").toString();
        assertTrue("Invalid token", getLooseTouchTokenProvider().getAccountId(looseTouchAccount2Token1).isPresent());
        assertTrue("Invalid account", getAccountRepository().getAccount(getLooseTouchTokenProvider().getAccountId(looseTouchAccount2Token1).get()).isPresent());

        Thread.sleep(TimeUnit.SECONDS.toMillis(1));

        // Getting again the profile (with same google token).
        result2 = getMockMvc().perform(get(ACCOUNT_PROFILE_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + getLooseToucheToken(GOOGLE_ACCOUNT_2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idToken").isNotEmpty())
                .andExpect(jsonPath("firstName").value(GOOGLE_ACCOUNT_2.getFirstName()))
                .andExpect(jsonPath("lastName").value(GOOGLE_ACCOUNT_2.getLastName()))
                .andExpect(jsonPath("email").value(GOOGLE_ACCOUNT_2.getEmail()))
                .andExpect(jsonPath("pictureUrl").isString())
                .andExpect(jsonPath("newAccount").value(false))
                .andReturn();

        // Check that the returned token is ok and it's the good account.
        String looseTouchAccount2Token2 = JsonPath.parse(result2.getResponse().getContentAsString()).read("idToken").toString();
        assertNotEquals("The same token was retrieved", looseTouchAccount2Token1, looseTouchAccount2Token2);
        assertTrue("Invalid token", getLooseTouchTokenProvider().getAccountId(looseTouchAccount2Token2).isPresent());
        assertTrue("Invalid account", getAccountRepository().getAccount(getLooseTouchTokenProvider().getAccountId(looseTouchAccount2Token2).get()).isPresent());
    }

}
