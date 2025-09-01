package com.hodol.toy_hodol.common.resolver;

import com.hodol.toy_hodol.common.exception.CustomException;
import com.hodol.toy_hodol.common.exception.ErrorCode;
import com.hodol.toy_hodol.common.resolver.data.UserSession;
import com.hodol.toy_hodol.config.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final AppConfig appConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String jws = webRequest.getHeader("Authorization");
        if ( jws == null || jws.isEmpty() ) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }


        try {
            SecretKey secretKey = appConfig.getSecretKey();

            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jws);

            String userId = claims
                    .getPayload()
                    .getSubject();

            return new UserSession(Long.parseLong(userId));
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
}
