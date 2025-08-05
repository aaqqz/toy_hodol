package com.hodol.toy_hodol.common.resolver;

import com.hodol.toy_hodol.common.resolver.data.UserSession;
import com.hodol.toy_hodol.common.exception.CustomException;
import com.hodol.toy_hodol.common.exception.ErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getHeader("Authorization");
        if ( accessToken == null || accessToken.isEmpty() ) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return UserSession.builder()
                .id(1L)
                .build();
    }
}
