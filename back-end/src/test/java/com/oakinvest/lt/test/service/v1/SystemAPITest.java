package com.oakinvest.lt.test.service.v1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * System API test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SystemAPITest {

    /**
     * Mock mvc.
     */
    @Autowired
    private MockMvc mvc;

    /**
     * Ping test.
     */
    @Test
    public void pingTest() throws Exception {
        mvc.perform(get("/v1/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("pong")));
    }

    /**
     * GetStatus test.
     */
    @Test
    public void statusTest() throws Exception {
        mvc.perform(get("/v1/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("currentDate").isNotEmpty())
                .andExpect(jsonPath("numberOfUsers").value(0));
    }

}
