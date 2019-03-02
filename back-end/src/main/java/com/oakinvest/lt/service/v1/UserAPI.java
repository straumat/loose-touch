package com.oakinvest.lt.service.v1;

import com.oakinvest.lt.authentication.loosetouch.AuthenticatedUser;
import com.oakinvest.lt.dto.v1.UserDTO;
import com.oakinvest.lt.service.util.V1Service;
import com.oakinvest.lt.util.error.LooseTouchError;
import io.swagger.annotations.Api;
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
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_NOT_FOUND;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_NOT_FOUND_MESSAGE;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_OK;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_OK_MESSAGE;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_REQUEST_FAILED;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_REQUEST_FAILED_MESSAGE;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_UNAUTHORIZED;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_UNAUTHORIZED_MESSAGE;

/**
 * User API.
 */
@Api(tags = "User API")
public interface UserAPI extends V1Service {

    /**
     * Application login for google (if the user doesn't exists, creates it).
     *
     * @param googleIdToken     Google ID token
     * @param googleAccessToken Google access token
     * @return loose touch token
     */
    @RequestMapping(value = "/login/google",
            method = RequestMethod.GET)
    @ApiOperation(value = "Application login for google (if the user doesn't exists, creates it)",
            response = UserDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "googleIdToken",
                    dataType = "string",
                    value = "Google id token"),
            @ApiImplicitParam(name = "googleAccessToken",
                    dataType = "string",
                    value = "Google access token")
    })
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, message = STATUS_OK_MESSAGE, response = UserDTO.class),
            @ApiResponse(code = STATUS_BAD_REQUEST, message = STATUS_BAD_REQUEST_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_UNAUTHORIZED, message = STATUS_UNAUTHORIZED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_NOT_FOUND, message = STATUS_NOT_FOUND_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_REQUEST_FAILED, message = STATUS_REQUEST_FAILED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_INTERNAL_SERVER_ERROR, message = STATUS_INTERNAL_SERVER_ERROR_MESSAGE, response = LooseTouchError.class)
    })
    UserDTO googleLogin(@ApiParam(value = "Google id token")
                        @RequestParam(required = false) String googleIdToken,
                        @ApiParam(value = "Google access token")
                        @RequestParam(required = false) String googleAccessToken);

    /**
     * Get the user profile and a new refreshed token.
     *
     * @param authenticatedUser authenticated user
     * @return profile
     */
    @RequestMapping(value = "/profile",
            method = RequestMethod.GET)
    @ApiOperation(value = "Get the user profile and a new refreshed token",
            response = UserDTO.class)
    @ApiImplicitParams({})
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, message = STATUS_OK_MESSAGE, response = UserDTO.class),
            @ApiResponse(code = STATUS_BAD_REQUEST, message = STATUS_BAD_REQUEST_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_UNAUTHORIZED, message = STATUS_UNAUTHORIZED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_NOT_FOUND, message = STATUS_NOT_FOUND_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_REQUEST_FAILED, message = STATUS_REQUEST_FAILED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_INTERNAL_SERVER_ERROR, message = STATUS_INTERNAL_SERVER_ERROR_MESSAGE, response = LooseTouchError.class)
    })
    UserDTO getProfile(AuthenticatedUser authenticatedUser);

}
