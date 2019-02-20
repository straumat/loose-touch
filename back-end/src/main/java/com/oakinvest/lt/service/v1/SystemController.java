package com.oakinvest.lt.service.v1;

import com.oakinvest.lt.dto.Status;
import org.springframework.web.bind.annotation.RestController;

/**
 * System controller.
 */
@RestController
public class SystemController implements SystemAPI {

    /**
     * Ping.
     *
     * @return pong
     */
    @Override
    public final String ping() {
        return "pong";
    }

    /**
     * Returns application status.
     *
     * @return status
     */
    @Override
    public final Status getStatus() {
        Status s = new Status();
        // TODO Implement a real user count.
        s.setNumberOfUsers(0);
        return s;
    }

}
