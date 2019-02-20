package com.oakinvest.lt.service.v1;

import com.oakinvest.lt.dto.Status;
import com.oakinvest.lt.service.V1Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_OK;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_OK_MESSAGE;

/**
 * System API.
 */
@Api(tags = "System API")
public interface SystemAPI extends V1Service {

    /**
     * Ping.
     *
     * @return pong
     */
    @RequestMapping(path = "/ping", method = RequestMethod.GET)
    @ApiOperation(value = "Returns pong (keep alive method", response = String.class)
    @ApiImplicitParams({})
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, message = STATUS_OK_MESSAGE)
    })
    String ping();

    /**
     * Returns application status.
     *
     * @return status
     */
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ApiOperation(value = "Returns application status", response = Status.class)
    @ApiImplicitParams({})
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, message = STATUS_OK_MESSAGE)
    })
    Status getStatus();

}
