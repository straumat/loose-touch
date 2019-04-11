package com.oakinvest.lt.service.v1;

import com.oakinvest.lt.authentication.loosetouch.AuthenticatedAccount;
import com.oakinvest.lt.dto.v1.AccountDTO;
import com.oakinvest.lt.service.util.V1Service;
import com.oakinvest.lt.util.error.LooseTouchError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * Account API.
 */
@Api(tags = "Account API")
public interface AccountAPI extends V1Service {

    /**
     * Application login for google (if the account doesn't exists, creates it).
     *
     * @param googleIdToken     Google ID token
     * @param googleAccessToken Google access token
     * @return loose touch token
     */
    @RequestMapping(value = "/login/google",
            method = RequestMethod.GET)
    @ApiOperation(value = "Application login for google (if the account doesn't exists, creates it)",
            response = AccountDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "googleIdToken",
                    dataType = "string",
                    value = "Google id token"),
            @ApiImplicitParam(name = "googleAccessToken",
                    dataType = "string",
                    value = "Google access token")
    })
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, message = STATUS_OK_MESSAGE, response = AccountDTO.class),
            @ApiResponse(code = STATUS_BAD_REQUEST, message = STATUS_BAD_REQUEST_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_UNAUTHORIZED, message = STATUS_UNAUTHORIZED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_NOT_FOUND, message = STATUS_NOT_FOUND_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_REQUEST_FAILED, message = STATUS_REQUEST_FAILED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_INTERNAL_SERVER_ERROR, message = STATUS_INTERNAL_SERVER_ERROR_MESSAGE, response = LooseTouchError.class)
    })
    AccountDTO googleLogin(@ApiParam(value = "Google id token")
                        @RequestParam(required = false) String googleIdToken,
                           @ApiParam(value = "Google access token")
                        @RequestParam(required = false) String googleAccessToken);

    /**
     * Get the account profile and a new refreshed token.
     *
     * @param authenticatedAccount authenticated account
     * @return profile
     */
    @RequestMapping(value = "/account/profile",
            method = RequestMethod.GET)
    @ApiOperation(value = "Get the account profile and a new refreshed token",
            response = AccountDTO.class)
    @ApiImplicitParams({})
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, message = STATUS_OK_MESSAGE, response = AccountDTO.class),
            @ApiResponse(code = STATUS_BAD_REQUEST, message = STATUS_BAD_REQUEST_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_UNAUTHORIZED, message = STATUS_UNAUTHORIZED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_NOT_FOUND, message = STATUS_NOT_FOUND_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_REQUEST_FAILED, message = STATUS_REQUEST_FAILED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_INTERNAL_SERVER_ERROR, message = STATUS_INTERNAL_SERVER_ERROR_MESSAGE, response = LooseTouchError.class)
    })
    AccountDTO getProfile(AuthenticatedAccount authenticatedAccount);

    /**
     * Delete the account and all its data.
     *
     * @param authenticatedAccount account.
     */
    @DeleteMapping(value = "/account/delete")
    @ApiOperation(value = "Get the account profile and a new refreshed token")
    @ApiImplicitParams({})
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, message = STATUS_OK_MESSAGE),
            @ApiResponse(code = STATUS_BAD_REQUEST, message = STATUS_BAD_REQUEST_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_UNAUTHORIZED, message = STATUS_UNAUTHORIZED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_NOT_FOUND, message = STATUS_NOT_FOUND_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_REQUEST_FAILED, message = STATUS_REQUEST_FAILED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_INTERNAL_SERVER_ERROR, message = STATUS_INTERNAL_SERVER_ERROR_MESSAGE, response = LooseTouchError.class)
    })
    void delete(AuthenticatedAccount authenticatedAccount);

}
