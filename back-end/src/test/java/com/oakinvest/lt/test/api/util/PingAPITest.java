package com.oakinvest.lt.test.api.util;

import com.oakinvest.lt.test.util.junit.JUnitHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.oakinvest.lt.configuration.Application.LOCAL_DYNAMODB_ENVIRONMENT;
import static org.hamcrest.CoreMatchers.is;
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
public class PingAPITest extends JUnitHelper {

    /**
     * GetStatus test.
     */
    @Test
    public void statusTest() throws Exception {
        getMvc().perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("pong")));
    }

}
