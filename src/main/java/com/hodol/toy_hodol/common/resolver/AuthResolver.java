package com.hodol.toy_hodol.common.resolver;

import com.hodol.toy_hodol.common.exception.CustomException;
import com.hodol.toy_hodol.common.exception.ErrorCode;
import com.hodol.toy_hodol.common.resolver.data.UserSession;
import com.hodol.toy_hodol.domain.auth.entity.Session;
import com.hodol.toy_hodol.domain.auth.repository.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        String accessToken = webRequest.getHeader("Authorization");
//        if ( accessToken == null || accessToken.isEmpty() ) {
//            throw new CustomException(ErrorCode.UNAUTHORIZED);
//        }

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest == null){
            log.error("HttpServletRequest is null");
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Cookie[] cookies = servletRequest.getCookies();
        if (cookies.length == 0) {
            log.error("No cookies found in request");
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        String accessToken = cookies[0].getValue();

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        return UserSession.builder()
                .id(session.getUser().getId())
                .build();
    }
}
