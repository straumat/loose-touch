package com.oakinvest.lt.service.v1;

import com.oakinvest.lt.authentication.loosetouch.AuthenticatedUser;
import com.oakinvest.lt.dto.v1.UserDTO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User API.
 */
@Api(tags = "User API")
public interface UserAPI extends V1Service {

    /**
     * Get the user profile.
     * @param authenticatedUser authenticated user.
     * @return profil.
     */
    @RequestMapping(value = "/profile",
            method = RequestMethod.GET)
    UserDTO getProfile(AuthenticatedUser authenticatedUser);

}
