package com.oakinvest.lt.service.util;

import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_OK;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_OK_MESSAGE;

/**
 * Ping API.
 */
public interface PingAPI {

    /**
     * Ping.
     *
     * @return pong
     */
    @RequestMapping(path = "/ping", method = RequestMethod.GET)
    @ApiOperation(value = "Returns pong (keep alive method)", response = String.class)
    @ApiImplicitParams({})
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, message = STATUS_OK_MESSAGE)
    })
    String ping();

}
