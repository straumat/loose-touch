package com.oakinvest.lt.service.v1;

import com.oakinvest.lt.authentication.loosetouch.AuthenticatedUser;
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller.
 */
@RestController
public class UserController implements UserAPI {

    /**
     * Get the user profile.
     *
     * @param authenticatedUser authenticated user.
     * @return profil.
     */
    @Override
    public final String getProfile(final AuthenticatedUser authenticatedUser) {
        System.out.println("L'utilsiateur connecté est " + authenticatedUser.getUserId());
        return "test";
    }

}
