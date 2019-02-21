package com.oakinvest.lt.service.util;

import org.springframework.web.bind.annotation.RestController;

/**
 * Ping controller.
 */
@RestController
public class PingController implements PingAPI {

    @Override
    public final String ping() {
        return "pong";
    }

}
