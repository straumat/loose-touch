package com.oakinvest.lt.web.rest;

import com.oakinvest.lt.dto.Status;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_OK;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_OK_MESSAGE;

/**
 * Status API.
 */
@Api(tags = "Currency-pair information", description = " ")
public interface StatusAPI {

    /**
     * Returns application status.
     *
     * @return status
     */
    @RequestMapping(value = "/v1/status", method = RequestMethod.GET)
    @ApiOperation(value = "Returns application status", response = Status.class)
    @ApiImplicitParams({})
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, message = STATUS_OK_MESSAGE)
    })
    Status getStatus();

}
