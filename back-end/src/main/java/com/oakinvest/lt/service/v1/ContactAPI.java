package com.oakinvest.lt.service.v1;

import com.oakinvest.lt.authentication.loosetouch.AuthenticatedUser;
import com.oakinvest.lt.dto.v1.ContactDTO;
import com.oakinvest.lt.util.error.LooseTouchError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_BAD_REQUEST;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_BAD_REQUEST_MESSAGE;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_CREATED;
import static com.oakinvest.lt.util.rest.HttpStatus.STATUS_CREATED_MESSAGE;
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
 * Contact API.
 */
@Api(tags = "Contact API")
public interface ContactAPI extends V1Service {

    /**
     * Create a contact.
     *
     * @param authenticatedUser authenticated user
     * @param contact           contact to create
     * @return contact created
     */
    @PostMapping(value = "Create a new contact")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/contacts",
            method = RequestMethod.POST)
    @ApiOperation(value = "Create a new contact",
            response = ContactDTO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "contact",
            dataTypeClass = ContactDTO.class,
            value = "Contact to create")})
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_CREATED, message = STATUS_CREATED_MESSAGE, response = ContactDTO.class),
            @ApiResponse(code = STATUS_BAD_REQUEST, message = STATUS_BAD_REQUEST_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_UNAUTHORIZED, message = STATUS_UNAUTHORIZED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_NOT_FOUND, message = STATUS_NOT_FOUND_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_REQUEST_FAILED, message = STATUS_REQUEST_FAILED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_INTERNAL_SERVER_ERROR, message = STATUS_INTERNAL_SERVER_ERROR_MESSAGE, response = LooseTouchError.class)
    })
    ContactDTO create(AuthenticatedUser authenticatedUser,
                      @RequestBody(required = false) ContactDTO contact);

    /**
     * Get a contact.
     *
     * @param authenticatedUser authenticated user
     * @param email             email of the contact
     * @return contact
     */
    @GetMapping(value = "/contacts/{email:.+}")
    @ApiOperation(value = "Get a contact",
            response = ContactDTO.class)
    @ApiImplicitParams({@ApiImplicitParam(name = "email",
            dataTypeClass = String.class,
            value = "Email of the contact to find")})
    @ApiResponses(value = {
            @ApiResponse(code = STATUS_OK, message = STATUS_OK_MESSAGE, response = ContactDTO.class),
            @ApiResponse(code = STATUS_BAD_REQUEST, message = STATUS_BAD_REQUEST_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_UNAUTHORIZED, message = STATUS_UNAUTHORIZED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_NOT_FOUND, message = STATUS_NOT_FOUND_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_REQUEST_FAILED, message = STATUS_REQUEST_FAILED_MESSAGE, response = LooseTouchError.class),
            @ApiResponse(code = STATUS_INTERNAL_SERVER_ERROR, message = STATUS_INTERNAL_SERVER_ERROR_MESSAGE, response = LooseTouchError.class)
    })
    ContactDTO getContact(AuthenticatedUser authenticatedUser,
                          @PathVariable("email") String email);


}
