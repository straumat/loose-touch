package com.oakinvest.lt.service.v1;

import com.oakinvest.lt.util.error.LooseTouchError;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_BAD_REQUEST;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_BAD_REQUEST_MESSAGE;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_INTERNAL_SERVER_ERROR;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_INTERNAL_SERVER_ERROR_MESSAGE;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_OK;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_OK_MESSAGE;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_REQUEST_FAILED;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_REQUEST_FAILED_MESSAGE;

/**
 * Login API.
 */
public interface LoginAPI extends V1Service {

    /**
     * Application login for google (if the user doesn't exists, creates it).
     *
     * @param googleIdToken Google ID token
     * @return loose touch token
     */
    @RequestMapping(value = "/login/google",
            method = RequestMethod.GET)
    @ApiOperation(value = "Application login for google (if the user doesn't exists, creates it)",
            response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "googleIdToken",
                    dataType = "string",
                    value = "Google ID token")
    })
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, message = STATUS_OK_MESSAGE),
            @ApiResponse(code = STATUS_BAD_REQUEST, message = STATUS_BAD_REQUEST_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_REQUEST_FAILED, message = STATUS_REQUEST_FAILED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_INTERNAL_SERVER_ERROR, message = STATUS_INTERNAL_SERVER_ERROR_MESSAGE, response = LooseTouchError.class)
    })
    String googleLogin(@ApiParam(value = "Google ID token")
                       @RequestParam(required = false) String googleIdToken);

}
