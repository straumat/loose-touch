package com.oakinvest.lt.service.util;

import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Ping controller.
 */
@SuppressWarnings("unused")
@RestController
public class PingController implements PingAPI {

    @Override
    public final String ping() {
        ZoneId zoneId = ZoneId.of("Europe/Paris");
        ZonedDateTime zt = ZonedDateTime.now(zoneId);

        System.out.println(zt.toString());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm:ssZ");
        String val = dtf.format(zt);

        return "pong at " + val;
    }

}
