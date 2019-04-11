package com.oakinvest.lt.authentication.loosetouch;

import com.oakinvest.lt.util.error.LooseTouchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Optional;

import static com.oakinvest.lt.configuration.Application.BEARER_TYPE;
import static com.oakinvest.lt.configuration.Application.LOOSE_TOUCH_TOKEN_PARAMETER;
import static com.oakinvest.lt.configuration.Application.ACCOUNT_ID_PARAMETER;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.authentication_error;
import static com.oakinvest.lt.util.error.LooseTouchErrorType.invalid_request_error;

/**
 * Authentication interceptor.
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    /**
     * Loose touch token provider.
     */
    @Autowired
    private LooseTouchTokenProvider looseTouchTokenProvider;

    @SuppressWarnings("RedundantThrows")
    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        Optional<String> idToken = extractHeaderToken(request);
        if (idToken.isPresent()) {
            Optional<String> accountId = looseTouchTokenProvider.getAccountId(idToken.get());
            if (accountId.isPresent()) {
                // We set the token & accountId as parameter.
                request.setAttribute(LOOSE_TOUCH_TOKEN_PARAMETER, idToken.get());
                request.setAttribute(ACCOUNT_ID_PARAMETER, accountId.get());
            } else {
                // Invalid in the headers.
                throw new LooseTouchException(authentication_error, "Invalid loose touch token : " + idToken.get());
            }
        } else {
            // No token in the headers.
            throw new LooseTouchException(invalid_request_error, "Loose touch token missing");
        }
        return true;
    }

    /**
     * Extract the OAuth bearer token from a header.
     *
     * @param request The request.
     * @return The token, or null if no OAuth authorization header was supplied.
     */
    private Optional<String> extractHeaderToken(final HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()) { // typically there is only one (most servers enforce that)
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return Optional.of(authHeaderValue);
            }
        }
        return Optional.empty();
    }

}
