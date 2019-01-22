package com.oakinvest.lt.web.rest;

import com.oakinvest.lt.dto.Status;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Status API.
 */
public interface StatusAPI {

    /**
     * Returns application status.
     *
     * @return status
     */
    @RequestMapping(value = "/v1/status",
            method = RequestMethod.GET)
    Status getStatus();

}
