package com.oakinvest.lt.service.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oakinvest.lt.authentication.loosetouch.AuthenticatedUser;
import com.oakinvest.lt.domain.User;
import com.oakinvest.lt.dto.v1.UserDTO;
import com.oakinvest.lt.repository.UserRepository;
import com.oakinvest.lt.util.error.LooseTouchException;
import com.oakinvest.lt.util.mapper.LooseTouchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.api_error;

/**
 * User controller.
 */
@JsonInclude(NON_NULL)
@RestController
public class UserController implements UserAPI {

    /**
     * User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Get the user profile.
     *
     * @param authenticatedUser authenticated user
     * @return profile
     */
    @Override
    public final UserDTO getProfile(final AuthenticatedUser authenticatedUser) {
        Optional<User> user = userRepository.getUser(authenticatedUser.getUserId());
        if (user.isPresent()) {
            return LooseTouchMapper.INSTANCE.userToUserDTO(user.get());
        } else {
            throw new LooseTouchException(api_error, "User " + authenticatedUser.getUserId() + " not found");
        }
    }

}
