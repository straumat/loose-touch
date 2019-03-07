package com.oakinvest.lt.authentication.loosetouch;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.oakinvest.lt.configuration.Application.LOOSE_TOUCH_TOKEN_PARAMETER;
import static com.oakinvest.lt.configuration.Application.USER_ID_PARAMETER;

/**
 * Authentication user resolver.
 */
public class AuthenticationUserResolver implements HandlerMethodArgumentResolver {

    @Override
    public final boolean supportsParameter(final MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(AuthenticatedUser.class);
    }

    @Override
    public final Object resolveArgument(final MethodParameter methodParameter, final ModelAndViewContainer modelAndViewContainer, final NativeWebRequest nativeWebRequest, final WebDataBinderFactory webDataBinderFactory) throws Exception {
        String userId = (String) nativeWebRequest.getAttribute(USER_ID_PARAMETER, RequestAttributes.SCOPE_REQUEST);
        String looseTouchToken = (String) nativeWebRequest.getAttribute(LOOSE_TOUCH_TOKEN_PARAMETER, RequestAttributes.SCOPE_REQUEST);
        return new AuthenticatedUser(looseTouchToken, userId);
    }

}
