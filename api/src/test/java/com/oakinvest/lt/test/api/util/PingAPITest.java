package com.oakinvest.lt.test.api.util;

import com.oakinvest.lt.test.util.api.APITest;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.oakinvest.lt.configuration.Application.LOCAL_DYNAMODB_ENVIRONMENT;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Ping api test.
 */
@ActiveProfiles(LOCAL_DYNAMODB_ENVIRONMENT)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PingAPITest extends APITest {

    @Ignore("Useless for ping")
    @Override
    public void authenticationTest() {

    }

    @Ignore("Useless for ping")
    @Override
    public void validDataTest() {

    }

    @Override
    public void businessLogicTest() throws Exception {
        getMvc().perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("pong at")));
    }

}
