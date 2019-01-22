package com.oakinvest.lt.web.rest;

import com.oakinvest.lt.dto.Status;
import org.springframework.web.bind.annotation.RestController;

/**
 * Status controller.
 */
@RestController
public class StatusController implements StatusAPI {

    /**
     * Returns application status.
     *
     * @return status
     */
    @Override
    public final Status getStatus() {
        Status s = new Status();
        // TODO Implement a real user count.
        s.setUsersCount(1);
        return s;
    }

}
