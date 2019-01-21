package com.oakinvest.lt.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.Map;


/**
 * Ping controller.
 */
@RestController
@EnableWebMvc
public class PingController {

    /**
     * Ping.
     *
     * @return pong
     */
    @RequestMapping(path = "/ping", method = RequestMethod.GET)
    public final Map<String, String> ping() {
        Map<String, String> pong = new HashMap<>();
        pong.put("pong", "Hello, World!");
        return pong;
    }

}
